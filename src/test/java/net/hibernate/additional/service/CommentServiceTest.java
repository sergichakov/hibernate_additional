package net.hibernate.additional.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import net.hibernate.additional.command.CommentCommandDTO;
import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.command.mapper.CommentCommandDtoEntityMapper;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapper;
import net.hibernate.additional.dto.CommentDTO;
import net.hibernate.additional.model.CommentEntity;
import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.model.UserEntity;
import net.hibernate.additional.object.SessionObject;
import net.hibernate.additional.object.TaskStatus;
import net.hibernate.additional.repository.SessionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentServiceTest {
    CommentService commentService;
    CommentEntity commentEntity;
    static SessionObject sessionObject=null;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withUsername("anton")
            .withPassword("anton")
            .withReuse(true)///testcontainers.reuse.enable=true	//   ~/.testcontainers.properties
            .withDatabaseName("postgres");
    @BeforeAll
    static void beforeAll() {//static void beforeAll
        sessionObject= SessionObject.builder()
                .name("ADMIN")
                .password("ADMIN")
                .build();
        postgres.start();

        //createTableConsistency();
    }
    @AfterEach
    void tearDown() throws SQLException, LiquibaseException {
        Connection connection= DriverManager.getConnection(//"jdbc:postgresql://localhost:5432/testdb", "test","test");
                postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword());
        Database dataBase= DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase=new Liquibase("liquibase/dev/dbchangelog.xml",new ClassLoaderResourceAccessor(),dataBase);
        liquibase.rollback("initialState","legacy");
        dataBase.close();
        connection.close();

        System.out.printf("liquibase rollback");
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

        commentService =new CommentService(new CommentServiceTest.SessionRepo());
        //TaskEntity taskEntity=null;
        TaskEntity taskEntity=null;
        try(Session session=new CommentServiceTest.SessionRepo().getSession().openSession()){
            Transaction transaction=session.beginTransaction();
            UserEntity userEntity=UserEntity.builder()
                    .userName("ADMIN")
                    .password("ADMIN")
                    .build();
            commentEntity=CommentEntity.builder()
                    .comment("First Comment")
                    .user(userEntity)
                    .build();
            //TaskEntity
            taskEntity= TaskEntity.builder()
                    .name("task1")
                    .comments(Arrays.asList(commentEntity))
                    .user(userEntity)
                    .status(TaskStatus.IN_PROGRESS)
                    .createDate(new Date(1212121212121L))
                    .build();
            commentEntity.setTask(taskEntity);
            TagEntity tagEntity=TagEntity.builder()
                    .str("tag1")
                    .task(Set.of(taskEntity))
                    .build();
            taskEntity.setTag(Set.of(tagEntity));
            session.persist(commentEntity);
            session.persist(tagEntity);
            session.persist(userEntity);
            session.persist(taskEntity);
            //taskEntity.setCreateDate(new Date(1212121212121L));//////////////
            //session.merge(taskEntity);                          /////////////

            //session.flush();

            /*session.refresh(taskEntity);
            session.flush();
            taskEntity.setCreateDate(new Date(1212121212121L));//////////////
            session.merge(taskEntity);
            */
            transaction.commit();
        }

        try(Session session=new CommentServiceTest.SessionRepo().getSession().openSession()) {

            Transaction transaction = session.beginTransaction();
            taskEntity.setCreateDate(new Date(1212121212121L));
            session.merge(taskEntity);
            transaction.commit();
            /*Query query=session.createNativeQuery("INSERT INTO tasks (create_date) VALUES "
                            +"(:date);",TaskEntity.class);
            query.setParameter("date",new Date(1212121212121L));
            query.executeUpdate();

             */
            Query<TaskEntity> newTask=session.createQuery("from TaskEntity",TaskEntity.class);
            List<TaskEntity> newTaskEntity=newTask.list();//getSingleResult();
            for(TaskEntity ta:newTaskEntity){
                System.out.println("EDIT task ta="+ta);
            }
            //newTaskEntity.setCreateDate(new Date(1212121212121L));
            //session.merge(newTaskEntity);
/*
            session.refresh(taskEntity);
            session.flush();
            taskEntity.setCreateDate(new Date(1212121212121L));//////////////
            session.merge(taskEntity);                              ////////

*/
            //transaction.commit();
        }


        System.out.printf("liquibase rebuild");


    }

    @Test
    void listAllComments() {
        Long taskId=0L;
        TaskEntity taskEntity=null;
        TaskCommandDTO taskCommandDTO=null;
        String userName="";
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query<TaskEntity> newTask = session.createQuery("from TaskEntity", TaskEntity.class);
            taskEntity = newTask.list().get(0);
            //taskId=taskEntity.getTask_id();
            userName=taskEntity.getUser().getUserName();
            TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;
            taskCommandDTO=commandToEntityMapper.toDTO(taskEntity);
        }

        List<CommentDTO> commentDTOList= commentService.listAllComments(taskCommandDTO, userName);
        assertEquals("First Comment", commentDTOList.get(0).getComment());
    }

    @Test
    void editComment() {
        CommentCommandDTO commentCommandDTO=null;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query<CommentEntity> editComment = session.createQuery("from CommentEntity", CommentEntity.class);
            CommentEntity commentEntity = editComment.list().get(0);
            commentEntity.setComment("changed Comment");
            //taskId=taskEntity.getTask_id();
            //userName=taskEntity.getUser().getUserName();
            CommentCommandDtoEntityMapper entityToCommandMapper=CommentCommandDtoEntityMapper.INSTANCE;
            commentCommandDTO=entityToCommandMapper.toDTO(commentEntity);
        }
        assertTrue(commentService.editComment(commentCommandDTO));
        String changedComment="";
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query<CommentEntity> editComment = session.createQuery("from CommentEntity", CommentEntity.class);
            CommentEntity commentEntity = editComment.list().get(0);
            changedComment = commentEntity.getComment();
        }
        assertEquals("changed Comment",changedComment);

    }

    @Test
    void createComment() {
        CommentCommandDTO commentCommandDTO=null;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query<TaskEntity> newTask = session.createQuery("from TaskEntity", TaskEntity.class);
            TaskEntity taskEntity = newTask.list().get(0);
            CommentEntity commentEntity=CommentEntity.builder()
                    .comment("Second comment")
                    .task(taskEntity)
                    .build();
            CommentCommandDtoEntityMapper entityToCommandMapper=CommentCommandDtoEntityMapper.INSTANCE;
            commentCommandDTO=entityToCommandMapper.toDTO(commentEntity);
        }
        commentService.createComment(commentCommandDTO);
        Long count=null;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query countQuery=session.createNativeQuery("select count(*) from comments");
            count=(Long)countQuery.getSingleResultOrNull();
        }
        assertEquals(2,count);
    }

    @Test
    void deleteComment() {
        CommentCommandDTO commentCommandDTO=null;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query<TaskEntity> newTask = session.createQuery("from TaskEntity", TaskEntity.class);
            CommentEntity commentEntity = newTask.list().get(0).getComments().get(0);
            /*CommentEntity commentEntity=CommentEntity.builder()
                    .comment("Second comment")
                    .task(taskEntity)
                    .build();

             */
            CommentCommandDtoEntityMapper entityToCommandMapper=CommentCommandDtoEntityMapper.INSTANCE;
            commentCommandDTO=entityToCommandMapper.toDTO(commentEntity);
        }
        commentService.deleteComment(commentCommandDTO);
        Long count=null;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query countQuery=session.createNativeQuery("select count(*) from comments");
            count=(Long)countQuery.getSingleResultOrNull();
        }
        assertEquals(0,count);
    }
    private static class SessionRepo implements SessionRepository {
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
}