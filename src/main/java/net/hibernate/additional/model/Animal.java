package net.hibernate.additional.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.Session;
        import org.hibernate.SessionFactory;
        import org.hibernate.Transaction;
import java.util.Properties;


//import jakarta.persistence.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

@Data
@Entity
@Table(name = "animal_table")//, schema = "test")
public class Animal {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private Integer age;
    @Column(name = "family")
    private String family;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}

class MySessionFactory {
    private static MySessionFactory instance;
    private final SessionFactory sessionFactory;
    private MySessionFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:25432/demo");
        properties.setProperty("hibernate.connection.username", "anton");
        properties.setProperty("hibernate.connection.password", "anton");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.show_sql", "true");
        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Animal.class)
                .buildSessionFactory();
    }
    public static SessionFactory getSessionFactory() {
        if (instance == null) {
            instance = new MySessionFactory();
        }
        return instance.sessionFactory;
    }
}

class Solution {
    public static Animal animalCat = new Animal();
    public static Animal animalMouse = new Animal();
    public static Animal animalRemove;

    public static void main(String[] args) throws Exception {
        animalCat.setId(1L);
        animalCat.setName("Tom");
        animalCat.setAge(5);
        animalCat.setFamily("Cats");

        animalMouse.setId(2L);
        animalMouse.setName("Jerry");
        animalMouse.setAge(3);
        animalMouse.setFamily("Mice");

        try {
            SessionFactory sessionFactory = MySessionFactory.getSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(animalCat);
            session.persist(animalMouse);
            Query q=session.createQuery("delete from Animal where id=:iddd");
            q.setParameter("iddd",2);
            int l = q.executeUpdate();
            System.out.println(l);
            Query<Animal> q2=session.createQuery("from Animal",Animal.class);
            q2.getResultList();
            for (Animal a :q2.getResultList()){
                System.out.println(a);
            }
            //напишите тут ваш код
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}