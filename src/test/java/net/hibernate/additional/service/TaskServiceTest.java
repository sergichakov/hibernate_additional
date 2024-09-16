package net.hibernate.additional.service;

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
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;


import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {
    TaskService taskService;
    TaskEntity taskEntity;
    static SessionObject sessionObject=null;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withUsername("anton")
            .withPassword("anton")
            .withReuse(true)///testcontainers.reuse.enable=true	//   ~/.testcontainers.properties
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
        sessionObject= SessionObject.builder()
                .name("ADMIN")
                .password("ADMIN")
                .build();
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
//    @AfterAll
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

        taskService=new TaskService(new SessionRepo());
        //TaskEntity taskEntity=null;
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
            //TaskEntity
                    taskEntity=TaskEntity.builder()
                    .name("task1")
                    .comments(List.of(commentEntity))
                    .user(userEntity)
                    .status(TaskStatus.IN_PROGRESS)
                    .createDate(new Date(1212121212121L))
                    .build();
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

        try(Session session=new SessionRepo().getSession().openSession()) {

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

    @Test
    void listAllTasks() throws AuthenticationException {
/*        SessionObject sessionObject= SessionObject.builder()
                .name("ADMIN")
                .password("ADMIN")
                .build();                                   */
        //taskService=new TaskService(new SessionRepo());//kill this line from here !!!
        //try
testConsistency("before taskDTO");
        SessionRepository sessRepo= new SessionRepo();
        //try (Session session = sessRepo.getSession().openSession()){}

            List<TaskDTO> taskDtoList = taskService.listAllTasks(sessionObject, 0, 3);
testConsistency("After everything");
        System.out.println("taskDTO: "+taskDtoList);
        assertEquals("task1",taskDtoList.get(0).getName());
        //"[TaskDTO(task_id=1, name=task1, createDate=2008-05-30 08:20:12.121, startDate=null, endDate=null, status=IN_PROGRESS, user=UserDTO(user_id=1, userName=ADMIN, task=null), title=null, tag=[TagDTO(tag_id=1, str=tag1, task=null)])]", taskDtoList.toString());
    }
    @Test
    void editTask() throws AuthenticationException, NoPermissionException {
        /*SessionObject sessionObject= SessionObject.builder()
                .name("ADMIN")
                .password("ADMIN")
                .build();       */
        //TaskEntity taskEntity=null;
        TaskCommandDTO taskCommandDTO=null;//TaskCommandDTO.builder().;
        String taskChangedTitle="";
        try(Session session=new SessionRepo().getSession().openSession()) {
/*            Transaction transaction=session.beginTransaction();
            Query<TaskEntity> newTask = session.createQuery("from TaskEntity", TaskEntity.class);
            TaskEntity taskEntity = newTask.list().get(0);//newTask.getSingleResult();
*/
            taskEntity.setTitle("TITLE_1");

            TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;
            taskCommandDTO=commandToEntityMapper.toDTO(taskEntity);
            session.refresh(taskEntity);
            taskService.editTask(taskCommandDTO,sessionObject);
            //taskCommandDTO.setTitle("TITLE_1");
            //for (taskCommandDTO.getTag();

            //taskEntity.setTitle("new Title");//setStatus(TaskStatus.NOT_STARTED);
            //session.persist(taskEntity);
 //           transaction.commit();
            //Query<TaskEntity> newTask = session.createQuery("from TaskEntity", TaskEntity.class);
            //TaskEntity task=newTask.list().get(0);
            //taskChangedTitle = task.getTitle();
            Query titleQuery=session.createNativeQuery("select task_title from tasks t where name=:task1");
            titleQuery.setParameter("task1","task1");
            taskChangedTitle =(String) titleQuery.list().get(0);//getSingleResult();
        }
        System.out.println("edit task taskCommandDTO="+taskCommandDTO);
        assertEquals("TITLE_1",taskChangedTitle);

    }
    @Test
    void createTask() throws AuthenticationException, NoPermissionException {
        TaskCommandDTO taskCommandDTO=TaskCommandDTO.builder()
                .name("task 2")
                .build();
        TaskDTO taskDTO=taskService.createTask(taskCommandDTO,sessionObject);
        System.out.println("createTask"+taskDTO);
        assertEquals("task 2",taskDTO.getName());
    }
    @Test
    void deleteTask() throws AuthenticationException, NoPermissionException {
        TaskCommandDTO taskCommandDTO=null;
        int emptiNess=0;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query<TaskEntity> newTask = session.createQuery("from TaskEntity", TaskEntity.class);
            TaskEntity taskEntity = newTask.list().get(0);
            TaskCommandDtoEntityMapper commandToEntityMapper = TaskCommandDtoEntityMapper.INSTANCE;
            taskCommandDTO = commandToEntityMapper.toDTO(taskEntity);
            taskService.deleteTask(taskCommandDTO,sessionObject);

            Query<TaskEntity> empty = session.createQuery("from TaskEntity", TaskEntity.class);
            emptiNess = empty.list().size();
        }
        System.out.println("deleteTask="+emptiNess);
        assertEquals(0,emptiNess);
    }
    @Test
    void getAllCount() {
        Long count=null;
        try(Session session=new SessionRepo().getSession().openSession()) {
            Query query = session.createQuery("select count(*) from TaskEntity ");
            count=(Long)query.uniqueResult();
            System.out.println("getAllCount "+count);
        }
        assertEquals(1,count);
    }
    @Test
    void getIdOfTag() {
        String tagTitle="tag1";
        Long resultLong=0L;
        Long tagIndex=taskService.getIdOfTag(tagTitle);
        try(Session session=new SessionRepo().getSession().openSession()) {
            NativeQuery<Long> lon=session.createNativeQuery("select tag_id from tags where str=:tagStr",Long.class);
            lon.setParameter("tagStr",tagTitle);
            resultLong=lon.getSingleResultOrNull();
            //System.out.println("getAllCount "+count);
        }
        assertEquals(resultLong,tagIndex);
    }
    @Test
    void getAuthenticatedUser() throws AuthenticationException, NoPermissionException {
        UserDTO userDTO=taskService.getAuthenticatedUser(sessionObject);
        assertEquals("ADMIN",userDTO.getUserName());
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
    void start_script() throws SQLException, LiquibaseException {
        setUp();
        System.out.println("StartScript");
        tearDown();
    }
}