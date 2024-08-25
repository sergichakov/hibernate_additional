    package net.hibernate.additional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.mapper.TaskEntityDtoMapper;
import net.hibernate.additional.mapper.TaskEntityDtoMapperImpl;
import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.mapper.*;
import net.hibernate.additional.model.*;
/*
import dev.andrylat.hiber.model.Device;
import dev.andrylat.hiber.repository.DevicesRepository;
import dev.andrylat.hiber.session_provider.DevicesDbEmptyProvider;
import dev.andrylat.hiber.session_provider.DevicesDbNotEmptyProvider;
import dev.andrylat.hiber.session_provider.ManualConfigDefinitionSessionProvider;
import dev.andrylat.hiber.session_provider.PropertiesSessionProvider;
import dev.andrylat.hiber.session_provider.SessionProvider;
import dev.andrylat.hiber.session_provider.XmlConfigSessionProvider;
*/
import static org.junit.jupiter.api.Assertions.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.*;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.*;
import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args)throws IOException {
        // Different configurations
        //SessionProvider provider = new PropertiesSessionProvider();

        //SessionProvider provider = new XmlConfigSessionProvider();

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:25432/demo");
        properties.setProperty("hibernate.connection.username", "anton");
        properties.setProperty("hibernate.connection.password", "anton");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.show_sql", "true");

        SessionFactory sessionFactory=new Configuration()
                //.configure()
                .setProperties(properties)//.addPackage("net.hibernate_additional.repository")
                .addAnnotatedClass(TaskEntity.class)
                .addAnnotatedClass(TagEntity.class)
                //.addAnnotatedClass(CommentEntity.class)
                .buildSessionFactory();


        try (Session session = sessionFactory.openSession()) {

            Query<TaskEntity> query = session.createQuery("from TaskEntity", TaskEntity.class);
            //query.setParameterList
            List<TaskEntity> devices = query.list();
            for (TaskEntity device : devices) {
                System.out.println("Device: " + device);
            }
        }
/*        */
        TaskEntityDtoMapper taskMapper=new TaskEntityDtoMapperImpl() ;
        TaskEntity taskEntity=null;
        TaskDTO dto=null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            ///////////////CommentEntity commentsEntity= CommentEntity.builder().comment("I am OK").build();

            TagEntity tagEntity=TagEntity.builder().task(new HashSet<TaskEntity>()).build();

            taskEntity=TaskEntity.builder()
                    .name("wash shoes")
                    ///////////.comments(new HashSet<>())
                    .tag(new HashSet<>())
                    .build();
            ///////////////////////////commentsEntity.setTask(taskEntity);
            tagEntity.add(taskEntity);
            tagEntity.setStr("hallowu");
            taskEntity.addTag(tagEntity);
            ////////////////////taskEntity.addComment(commentsEntity);
            ////////////////////session.persist(commentsEntity);
            session.persist(taskEntity);

            dto=taskMapper.toDTO(taskEntity);


            tx.commit();
        }
        System.out.println(taskEntity.getName()+"entity ;THey BOth ArE equals dto="+dto.getName() );
        System.out.println("_nothing_"+taskEntity+"\n_nothing_"+dto);
        assertEquals(taskEntity.getName(),dto.getName());

        ObjectMapper om = new ObjectMapper();
        //om.configure(SerializationFeature.INDENT_OUTPUT, true);
        String fromDtoToJson="";
        try {
            fromDtoToJson=om.writeValueAsString(dto);
        }catch(JsonProcessingException e){
            System.out.println("EXCEPTION");
        }
        System.out.println("fromDtoToJson"+fromDtoToJson);

        TaskCommandDTO taskCommandDTO;
        try {
            taskCommandDTO = om.readValue(fromDtoToJson, TaskCommandDTO.class);
        }catch(JsonMappingException e){
            throw new IOException("Cant map JSon file",e);
        }catch(JsonProcessingException e){
            throw new IOException("Cant process JSon file",e);
        }
        System.out.println("taskCommand_DTO"+taskCommandDTO);


        //SessionProvider provider = new ManualConfigDefinitionSessionProvider(properties);

        /*SessionProvider provider = new ManualConfigDefinitionSessionProvider("hibernate-dev.cfg.xml");

        SessionFactory factory = provider.getSessionFactory();
        for (Map.Entry<String, Object> entry : factory.getProperties().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }*/



        // Mapping with different annotations
        /*SessionProvider devicesNotEmptyProvider = new DevicesDbNotEmptyProvider();
        SessionFactory sessionFactory = devicesNotEmptyProvider.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Query<Device> query = session.createQuery("from Device", Device.class);
            List<Device> devices = query.list();
            for (Device device : devices) {
                System.out.println("Device: " + device);
            }
        }*/

        // Hibernate will create DB structure
        /*SessionProvider devicesNotEmptyProvider = new DevicesDbEmptyProvider();
        SessionFactory sessionFactory = devicesNotEmptyProvider.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Query<Device> query = session.createQuery("from Device", Device.class);
            List<Device> devices = query.list();
            for (Device device : devices) {
                System.out.println("Device: " + device);
            }
        }*/

        // CRUD
        /*
        SessionProvider devicesNotEmptyProvider = new DevicesDbNotEmptyProvider();
        DevicesRepository devicesRepository = new DevicesRepository(devicesNotEmptyProvider.getSessionFactory());

        Device newDevice = Device.builder()
                .deviceCode("XXX")
                .name("OMDF-001")
                .connectionAmount(88)
                .installationDate(LocalDate.of(2020, 4, 29))
                .build();
        System.out.println("Device before adding: " + newDevice);

        Device addedDevice = devicesRepository.add(newDevice);
        System.out.println("Added device: " + addedDevice);

        devicesRepository.add(Device.builder()
                .deviceCode("YYYY")
                .name("OMDF-002")
                .connectionAmount(2)
                .installationDate(LocalDate.of(2021, 6, 1))
                .build());

        List<Device> allDevices = devicesRepository.getAll();
        for (Device device : allDevices) {
            System.out.println(device);
        }

        devicesRepository.remove(addedDevice);
        System.out.println("-------");

        allDevices = devicesRepository.getAll();
        for (Device device : allDevices) {
            System.out.println(device);
        }
        */

    }
}