package net.hibernate.additional.service;

import net.hibernate.additional.repository.SessionRepoHelper;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    TaskService taskService;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3")
            .withUsername("anton")
            .withPassword("anton")
            .withDatabaseName("postgres");
    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }
    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
    @BeforeEach
    void setUp() {
        taskService=new TaskService(new SessionRepoHelper());///////////////
    }
    @Test
    void justDoAnything(){

    }



    @AfterEach
    void tearDown() {
    }

    @Test
    void listAllTasks() {
    }

    @Test
    void editTask() {
    }

    @Test
    void createTask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void getAllCount() {
    }

    @Test
    void getIdOfTag() {
    }

    @Test
    void getAuthenticatedUser() {
    }
}