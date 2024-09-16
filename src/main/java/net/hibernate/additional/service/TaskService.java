package net.hibernate.additional.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapper;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.dto.UserDTO;
import net.hibernate.additional.exception.AuthenticationException;
import net.hibernate.additional.exception.NoPermissionException;
import net.hibernate.additional.mapper.TaskEntityDtoMapper;
import net.hibernate.additional.model.CommentEntity;
import net.hibernate.additional.object.SessionObject;
import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.model.UserEntity;
import net.hibernate.additional.object.TaskStatus;
import net.hibernate.additional.repository.SessionRepoHelper;
import net.hibernate.additional.repository.SessionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Order;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.*;
import java.util.*;
import java.util.Date;


//@NoArgsConstructor
public class TaskService {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withUsername("anton")
            .withPassword("anton")
            .withReuse(true)///testcontainers.reuse.enable=true	//   ~/.testcontainers.properties
            .withDatabaseName("postgres");
/*    public static void createTableConsistency()  {
        try(
                Connection connection= DriverManager.getConnection(postgres.getJdbcUrl(),
                        postgres.getUsername(),postgres.getPassword()))
        {
            String createTable="create table users ("
                       + "user_id      bigint, "

            +"password     varchar(255),"
                +"user_name    varchar(255),"
                +"task_task_id bigint)";
            String insertTable="INSERT INTO users (task_task_id,user_name, password) VALUES " +
                    "(2,'ADMIN', 'ADMIN');";

            PreparedStatement statCreate = connection.prepareStatement(createTable);
            Integer created = statCreate.executeUpdate();//.execute();

            PreparedStatement statement = connection.prepareStatement(insertTable);
            Integer resultSet = statement.executeUpdate();
            //while (resultSet.next()) {

            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void testConsistency(String str)  {
        try(
                Connection connection= DriverManager.getConnection(postgres.getJdbcUrl(),
                        postgres.getUsername(),postgres.getPassword()))
                         {
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet resultSet = statement.executeQuery();
            //while (resultSet.next()) {
            resultSet.next();
            System.out.println("preparedStatement1:" + resultSet.getString(1) + " 2:"
                    + resultSet.getString(2) + " 3:" + resultSet.getString(3)+" 4:"+ resultSet.getString(4));
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(str);
    }
    public static void main(String[] args) throws SQLException, LiquibaseException, AuthenticationException {
        postgres.start();
        TaskService taskService=new TaskService(new SessionRepo());
        taskService.setUp();
        //createTableConsistency();
        SessionObject sessionObject=SessionObject.builder()
                .name("ADMIN")
                .password("ADMIN")
                .build();
        testConsistency("before listAllTasks");
        try (Session session = (new SessionRepo()).getSession().openSession()) {}
        //taskService.listAllTasks(sessionObject,1,3);
        testConsistency("After listAllTasks");
        postgres.stop();
    }
    private static class SessionRepo implements SessionRepository{
        private static SessionFactory sessionFactory;

        static {
            Properties properties = new Properties();
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.setProperty("hibernate.connection.url", postgres.getJdbcUrl());//"jdbc:postgresql://localhost:"+postgres.getJdbcUrl()+"/postgres");
            properties.setProperty("hibernate.connection.username", postgres.getUsername());
            properties.setProperty("hibernate.connection.password", postgres.getPassword());
            properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
            properties.setProperty("hibernate.show_sql", "true");

            sessionFactory=new Configuration()

                    .setProperties(properties)//.addPackage("net.hibernate_additional.repository")
                    .addAnnotatedClass(TaskEntity.class)
                    .addAnnotatedClass(TagEntity.class)
                    .addAnnotatedClass(UserEntity.class)
                    .addAnnotatedClass(CommentEntity.class)

                    .buildSessionFactory();
        }
        public  SessionFactory getSession(){

            return sessionFactory;
        }
    }
*/
/*    void setUp() throws LiquibaseException, SQLException {
        //static PostgreSQLContainer<?> postgre=new PostgreSQLContainer<>();
        //Connection connection = DriverManager.getConnection(//"jdbc:postgresql://localhost:5432/testdb", "test","test");
        //        postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());

        //liquibase.close();
        //dataBase.close();
        //connection.close();
        try (Session session = new SessionRepo().getSession().openSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity userEntity = UserEntity.builder()
                    .userName("ADMIN")
                    .password("ADMIN")
                    .build();
            CommentEntity commentEntity = CommentEntity.builder()
                    .comment("First Comment")
                    .user(userEntity)
                    .build();
            TaskEntity taskEntity = TaskEntity.builder()
                    .name("task1")
                    .comments(List.of(commentEntity))
                    .user(userEntity)
                    .build();
            TagEntity tagEntity = TagEntity.builder()
                    .str("tag1")
                    .task(Set.of(taskEntity))
                    .build();
            taskEntity.setTag(Set.of(tagEntity));
            session.persist(userEntity);
            session.persist(taskEntity);
            transaction.commit();
        }
    }*/
    private Logger logger= null;
    private volatile SessionRepository sessionRepoHelper;

    public TaskService(){
        logger=LoggerFactory.getLogger(TaskService.class);
    }
    public TaskService(SessionRepository sessionRepoHelper){
        this.sessionRepoHelper=sessionRepoHelper;
        logger=LoggerFactory.getLogger(TaskService.class);
    }
    //public List<TaskDTO> listAllTasks(Integer page,Integer count){return null;}
    //public static void main(String[] args){Integer page=null;Integer count=null;
    public List<TaskDTO> listAllTasks(SessionObject sessionObject, Integer pageNumber, Integer pageSize) throws AuthenticationException {
        TaskEntityDtoMapper taskMapper=TaskEntityDtoMapper.INSTANCE;//new TaskEntityDtoMapperImpl() ;
        List<TaskEntity> taskEntities=null;

/*        Integer fullCount=getAllCount();
        if(pageNumber*pageSize<=fullCount){
            pageNumber=pageNumber-1;
        }*/
        List<TaskDTO> dtoList=new ArrayList<>();
        System.out.println("pageNumber="+pageNumber+" pageSize="+pageSize);
        if(pageSize==null)pageSize=3;
        if (pageSize>50)pageSize=50;
        if(pageNumber==null)pageNumber=0;
        /*UserEntity userEntity=null;
        try (Session session = sessionRepoHelper.getSession().openSession()) {
            Query<UserEntity> userEntityQuery=session.createQuery("from UserEntity where nameName = : userN",UserEntity.class);
            session.setProperty("userN",sessionObject.getName());
            userEntity=userEntityQuery.getSingleResultOrNull();
            if( ! userEntity.getPassword().equals(sessionObject.getPassword())){
                return null;
                //resp.sendRedirect(req.getContextPath() + "/redirected");
            }
        }*/                     ////new SessionRepoHelper()
        UserEntity userEntity=(new UserRegistrationService(sessionRepoHelper)).getUserEntity(sessionObject.getName(),sessionObject.getPassword());
        if (userEntity==null)throw new AuthenticationException("no such user="+sessionObject.getName()+" password="+sessionObject.getPassword());
        String userName= userEntity.getUserName();
//        testConsistency("before sessionRepohelper");///////////////////////////////////////
        try (Session session = sessionRepoHelper.getSession().openSession()) {
//            testConsistency("after tasks");
            Query<TaskEntity> tasks=null;

            if(userName==null||userName.equals("ADMIN") ||userName.isEmpty() || userName.equals("Unknown")){
                //request="from TaskEntity";
                tasks=session.createQuery("from TaskEntity ",TaskEntity.class);
            }else {
                tasks=session.createQuery("from TaskEntity where user= :userN",TaskEntity.class);
                System.out.println("userName="+userName);
                tasks.setParameter("userN",userEntity);
                System.out.println("tasks:="+tasks.list());
            }
            tasks.setOrder(Order.asc(TaskEntity.class,"task_id"));
            tasks.setFirstResult((pageNumber)*pageSize);
            tasks.setMaxResults(pageSize);
            taskEntities=tasks.list();

            for(TaskEntity taskEntity:taskEntities){
                //TaskEntity unProxy= (TaskEntity) Hibernate.unproxy(taskEntity);

                if (taskEntity.getStatus().equals(TaskStatus.IN_PROGRESS) && taskEntity.getEndDate()!=null){
                    checkExpired(taskEntity);
                }

                TaskDTO task=taskMapper.toDTO(taskEntity);
                dtoList.add(task);
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        //testConsistency("after sessionRepoHelper");
        return dtoList;
    }
    private void checkExpired(TaskEntity taskEntity){
        //System.out.println("listAllTasks status "+taskEntity.getStatus());
        if(taskEntity.getEndDate().before(new Date())){
            taskEntity.setStatus(TaskStatus.EXPIRED);
            /*try(Session session=sessionRepoHelper.getSession().openSession()){
                Transaction transaction=session.beginTransaction();
                //session.merge(taskEntity);
                transaction.commit();
            }*/
        }
    }
    public boolean editTask(TaskCommandDTO commandDTO, SessionObject sessionObject) throws AuthenticationException, NoPermissionException {
        TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;

        UserDTO userDTO= getAuthenticatedUser(sessionObject);
        if (!userDTO.getUserName().equals("ADMIN")) throw new NoPermissionException("user name= "+sessionObject.getName());
        Date dateOfCreation =null;
        //UserEntity userEntity=null;
        Boolean hasChanged=false;
        try(Session session = sessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);

            TaskEntity taskTemporary=new TaskEntity();
            //TaskEntity taskEntityResponse=null;
            Long task_id=taskEntity.getTask_id();
            session.load(taskTemporary,task_id);
            dateOfCreation =taskTemporary.getCreateDate();
            System.out.println("EditTask task_id="+taskEntity.getTask_id()+" dateOfCreation"+dateOfCreation);
            UserEntity userEntity=taskTemporary.getUser();

            taskEntity.setCreateDate(dateOfCreation);
            taskEntity.setUser(userEntity);
            session.merge(taskEntity);
            /*session.refresh(taskEntity);
            Set<TagEntity> tagEntitySet=taskEntity.getTag();
            for (TagEntity tag:tagEntitySet){
                tag.getTask().add(taskEntity);
            }
            System.out.println("taskService editTask="+taskEntity);
            session.merge(taskEntity);

             */
            //ttaskEntityaskEntityResponse=(TaskEntity) session.merge(taskEntity);
            if (task_id!=null) hasChanged=true;
            hasChanged=true;
            transaction.commit();
        }

        /*try(Session session = sessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.merge(taskEntity);
            //taskEntityResponse=(TaskEntity) session.merge(taskEntity);
            transaction.commit();
        }*/catch(ConstraintViolationException e){
            logger.info("attempt to insert new duplicate key into the table");
        }
        //return taskEntityResponse;
        //TaskEntity taskEntityResponse=editTaskProcessing(commandDTO);

        return hasChanged;
    }
    public TaskDTO createTask(TaskCommandDTO commandDTO,SessionObject sessionObject) throws AuthenticationException, NoPermissionException {
        TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
        UserDTO userDTO= getAuthenticatedUser(sessionObject);
        if (!userDTO.getUserName().equals("ADMIN")) throw new NoPermissionException();
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        TaskEntity taskEntityResponse;
        TaskDTO taskDTO=null;
        UserRegistrationService userRegistrationService=new UserRegistrationService(sessionRepoHelper);//new SessionRepoHelper());
        try(Session session = sessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            UserEntity userEntity=taskEntity.getUser();
            UserEntity userRegisteredEntity=null;
            if(userEntity!=null) {
                userRegisteredEntity = userRegistrationService.getUserEntity(userEntity.getUserName(), null);
                session.persist(userEntity);
            }

            taskEntityResponse=(TaskEntity) session.merge(taskEntity);
        //    transaction.commit();
        //}

        //TaskEntity taskEntityResponse=editTaskProcessing(commandDTO);
            System.out.println("taskEntity_createTask"+taskEntityResponse);
            if (taskEntityResponse==null)return null;
        //TaskDTO taskDTO=null;
        //try (Session session = sessionRepoHelper.getSession().openSession()) {
            //Transaction transaction=session.beginTransaction();

            session.persist(taskEntityResponse);//saveOrUpdate
            session.flush();
            session.refresh(taskEntityResponse);
            TaskEntityDtoMapper commandEntityMapper=TaskEntityDtoMapper.INSTANCE;//lnew TaskEntityDtoMapperImpl();
            taskDTO=commandEntityMapper.toDTO(taskEntityResponse);
            transaction.commit();
        }

        return taskDTO;
    }
    public boolean deleteTask(TaskCommandDTO commandDTO,SessionObject sessionObject) throws AuthenticationException, NoPermissionException {
        TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
        UserDTO userDTO= getAuthenticatedUser(sessionObject);
        if (!userDTO.getUserName().equals("ADMIN")) throw new NoPermissionException();
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        try(Session session = sessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.remove(taskEntity);
            transaction.commit();
        }
        return true;
    }

    public int getAllCount(SessionObject sessionObject) throws AuthenticationException, NoPermissionException {
        UserDTO userDTO=getAuthenticatedUser(sessionObject);

        try(Session session=sessionRepoHelper.getSession().openSession()){
            //Query<Long> query=session.createNamedQuery("namedQuery",Long.class);
            //query.setParameter(1,pageNumber);
            //query.setParameter(2,pageSize);
            Query<Long> query1=null;
            if (!userDTO.getUserName().equals("ADMIN")) {//throw new NoPermissionException();
                query1 = session.createQuery("select count(*) from TaskEntity where name = :strName", Long.class);
                query1.setParameter("strName",userDTO.getUserName());
            } else{
                query1 = session.createQuery("select count(*) from TaskEntity ", Long.class);
            }

            Integer i= query1.list().get(0).intValue();
            System.out.println("query namedQuery executed "+i);
            return i;//query.list().get(0).intValue();
        }
        //return 0;
    }
    public Long getIdOfTag(String tagStr){
        logger.info("getIdOfTag processing of string tagStr="+tagStr);
        TagEntity singleTagId=null;
        Long l=null;
        try(Session session=sessionRepoHelper.getSession().openSession()){
            //Query<TagEntity> queryTag=session.createQuery("from TagEntity t where t.str=:tagStr",TagEntity.class);
            //queryTag.setParameter("tagStr",tagStr);
            //singleTagId=queryTag.getSingleResult();
            NativeQuery<Long> lon=session.createNativeQuery("select tag_id from tags where str=:tagStr",Long.class);
            lon.setParameter("tagStr",tagStr);
            l=lon.getSingleResultOrNull();
            logger.info("something wrong in NativeQuery="+tagStr+ " long="+l );
            //Optional<Long> optLong=queryTag.uniqueResultOptional();
            //singleTagId=queryTag.uniqueResult();
        }
        if (singleTagId!=null)logger.info("for tag="+tagStr+" found number="+singleTagId);
        else System.out.println("singleTagId="+singleTagId);
        //return singleTagId.getTag_id();
        return l;
    }
    public UserDTO getAuthenticatedUser(SessionObject sessionObject) throws AuthenticationException, NoPermissionException {
        UserDTO userDTO=(new UserRegistrationService( sessionRepoHelper)).getUserDTO(sessionObject.getName(),sessionObject.getPassword());

        if (userDTO == null)throw new AuthenticationException("Get authenticated user");

        return userDTO;
    }
}
