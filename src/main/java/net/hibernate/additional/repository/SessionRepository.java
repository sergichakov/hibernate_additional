package net.hibernate.additional.repository;

import org.hibernate.SessionFactory;

public interface SessionRepository {
     SessionFactory getSession();
     //private static volatile SessionFactory sessionFactory=null;

}
