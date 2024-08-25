package net.hibernate.additional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Properties;

//@ApplicationPath("/api")
public class HelloApplication {
    public static void main(String[] args) {
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
                .setProperties(properties).addPackage("net.hibernate_additional.repository")
                .addAnnotatedClass(Device.class)
                //.addAnnotatedClass(TagEntity.class)
                //.addAnnotatedClass(CommentEntity.class)
                .buildSessionFactory();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction=session.beginTransaction();
            Query<Device> query = session.createQuery("insert into Device (connection) values (1),(2),(3)");
            System.out.println(query.executeUpdate());
            transaction.commit();
        }
        try (Session session = sessionFactory.openSession()) {

            Query<Long> query = session.createQuery("select sum(connection) from Device", Long.class);

            List<Long> devices = query.list();
            System.out.println(devices.get(0));
            for (Long device : devices) {
                System.out.println("Device: " + device);
            }
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Double> query=session.createQuery(
                    "select avg(connection) from Device",Double.class);

            //query.setFirstResult(offset);
            //query.setMaxResults(limit);
            System.out.println( query.list().get(0));
        }
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Device> query=session.createNativeQuery(
                    "select * from device order by id",Device.class);

            System.out.println( query.list());
        }
        /*try(Session session=MySessionFactory.getSessionFactory().openSession()){
            Query<Employee> query=session.createQuery(
                    "select sum(salary) from Employee",Employee.class);
            //query.setFirstResult(offset);
            //query.setMaxResults(limit);
            return query.list();
        }*/
    }
}
@Data
@Entity
class Device{
    @Id
    @GeneratedValue
    private int id;
    @Column(name="connections_amount")
    private int connection;

}