package org.app.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.omg.CORBA.Object;

import java.util.List;

/**
 * Created by lilit on 3/4/18.
 */
public class DBUtil {
    private static SessionFactory sessionFactory;

    /**
     * Initialized sessionFactory
     */
    static {
        try {

            StandardServiceRegistry standardRegistry =
                    new StandardServiceRegistryBuilder().configure("hibernate.xml").build();
            Metadata metaData =
                    new MetadataSources(standardRegistry).getMetadataBuilder().build();
             sessionFactory = metaData.getSessionFactoryBuilder().build();
        } catch (Throwable th) {
            System.err.println("Enitial SessionFactory creation failed" + th);
            throw new ExceptionInInitializerError(th);
        }
    }

    /**
     *  Saves or uodates records on
     *  corresponding table
     * @param objects
     * @param <T>
     */
    public static <T> void saveOrUpdate(List<T> objects){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            objects.stream().forEach(obj -> session.saveOrUpdate(obj));
            transaction.commit();

        }
    }
    /**10
     *  Saves or uodates records on
     *  corresponding table
     * @param object
     * @param <T>
     */
    public static <T> void saveOrUpdate(T object){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();

        }
    }

    /**
     * Returns object from db
     * by Id and class
     *
     * @param object
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> object, int id){
        try (Session session = sessionFactory.openSession()) {
            return session.get(object,id);
        }
    }

    /**
     * Loads limited Customers list,
     * limitation comes in request
     * from user as customer counts
     *
     * @param count
     * @param <T>
     * @return
     */
    public static <T> List<T> loadCustomers(int count){
        try (Session session = sessionFactory.openSession()) {
            return session.getNamedQuery("@HQL_GET_ALL_CUSTOMERS")
                    .setMaxResults(count)
                    .getResultList();
        }
    }

    /**
     *  Deletes records from
     *  DB corresponding table
     * @param objects
     * @param <T>
     */
    public static <T> void delete(List<T> objects){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            objects.stream().forEach(obj -> session.delete(obj));
            transaction.commit();

        }
    }

}
