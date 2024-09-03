package net.hibernate.additional.repository;
import net.hibernate.additional.model.CommentEntity;
import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.model.UserEntity;
import net.hibernate.additional.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class SessionRepoHelper {
    private static volatile SessionFactory sessionFactory;
    static {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:25432/sergej");
        properties.setProperty("hibernate.connection.username", "anton");
        properties.setProperty("hibernate.connection.password", "anton");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.show_sql", "true");

        sessionFactory=new Configuration()
                //.configure()
                .setProperties(properties)//.addPackage("net.hibernate_additional.repository")
                .addAnnotatedClass(TaskEntity.class)
                .addAnnotatedClass(TagEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(CommentEntity.class)
                //.registerTypeContributor( )
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(Message.class)
                .addAnnotatedClass(Users.class)
                /*.registerTypeContributor( (typeContributions, serviceRegistry) -> {
                    typeContributions.contributeType( UsersType.INSTANCE );

                })*/
                .buildSessionFactory();
    }
    public static SessionFactory getSession(){



        return sessionFactory;
    }
}
