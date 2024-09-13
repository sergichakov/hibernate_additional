package net.hibernate.additional.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.exception.AuthenticationException;
import net.hibernate.additional.model.CommentEntity;
import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.model.UserEntity;
import net.hibernate.additional.object.SessionObject;
import net.hibernate.additional.repository.SessionRepoHelper;
import net.hibernate.additional.repository.SessionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    TaskService taskService;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withUsername("anton")
            .withPassword("anton")
            //.withReuse(true)///testcontainers.reuse.enable=true	//   ~/.testcontainers.properties
            .withDatabaseName("postgres");
    public static void createTableConsistency()  {
        try(
                Connection connection= DriverManager.getConnection(
                        postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword()))
        {
            String createTable="create table users ("
                    + "user_id      bigint, "

                    +"password     varchar(255),"
                    +"user_name    varchar(255),"
                    +"task_task_id bigint)";
            String insertTable="INSERT INTO users (task_task_id,user_name, password) VALUES "
                    +"(2,'ADMIN', 'ADMIN'),"
                    +"(3,'Sergej', 'Sergej');";

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
    @BeforeAll
    static void beforeAll() {//static void beforeAll

        postgres.start();

        //createTableConsistency();
    }
    public static void testConsistency(String str)  {
        try(
        Connection connection= DriverManager.getConnection(//"jdbc:postgresql://localhost:5432/testdb", "test","test");
                postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet resultSet = statement.executeQuery();
            //while (resultSet.next()) {
            resultSet.next();
            System.out.println("preparedStatement       :" + resultSet.getString(1) + " 2"
                    + resultSet.getString(2)+ " 3" + resultSet.getString(3) + " 4" + resultSet.getString(4));
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(str);
    }
    @AfterAll
    static void afterAll()  {
        postgres.stop();
    }
    @BeforeEach
    void setUp() throws LiquibaseException, SQLException {
        Connection connection= DriverManager.getConnection(//"jdbc:postgresql://localhost:5432/testdb", "test","test");
        postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword());
        Database dataBase= DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase=new Liquibase("liquibase/dev/dbchangelog.xml",new ClassLoaderResourceAccessor(),dataBase);
        liquibase.update();
        //liquibase.close();
        dataBase.close();
        connection.close();


        try(Session session=new SessionRepo().getSession().openSession()){
            Transaction transaction=session.beginTransaction();
            UserEntity userEntity=UserEntity.builder()
                    .userName("ADMIN")
                    .password("ADMIN")
                    .build();
            CommentEntity commentEntity=CommentEntity.builder()
                    .comment("First Comment")
                    .user(userEntity)
                    .build();
            TaskEntity taskEntity=TaskEntity.builder()
                    .name("task1")
                    .comments(List.of(commentEntity))
                    .user(userEntity)
                    .build();
            TagEntity tagEntity=TagEntity.builder()
                    .str("tag1")
                    .task(Set.of(taskEntity))
                    .build();
            taskEntity.setTag(Set.of(tagEntity));
            session.persist(userEntity);
            session.persist(taskEntity);
            transaction.commit();
        }

        System.out.printf("liquibase rebuild");
        taskService=new TaskService(new SessionRepo());

    }

  // @AfterEach
    void tearDown() throws SQLException, LiquibaseException {
        Connection connection= DriverManager.getConnection(//"jdbc:postgresql://localhost:5432/testdb", "test","test");
                postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword());
        Database dataBase= DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase=new Liquibase("liquibase/dev/dbchangelog.xml",new ClassLoaderResourceAccessor(),dataBase);
        connection.close();
        dataBase.close();
        System.out.printf("liquibase rollback");
    }

    @Test
    void listAllTasks() throws AuthenticationException {
        SessionObject sessionObject= SessionObject.builder()
                .name("ADMIN")
                .password("ADMIN")
                .build();
        //taskService=new TaskService(new SessionRepo());//kill this line from here !!!
        //try
testConsistency("before taskDTO");
        SessionRepository sessRepo= new SessionRepo();
        try (Session session = sessRepo.getSession().openSession()){}

//            List<TaskDTO> taskDtoList = taskService.listAllTasks(sessionObject, 1, 3);
testConsistency("After everything");

    }


    private static class SessionRepo implements SessionRepository{
        public SessionFactory getSession(){
            System.setProperty("db.port", postgres.getFirstMappedPort().toString());
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
            return sessionFactory;
        }
    }
}