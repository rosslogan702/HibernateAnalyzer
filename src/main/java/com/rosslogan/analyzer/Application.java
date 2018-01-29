package com.rosslogan.analyzer;

import com.rosslogan.analyzer.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.xml.ws.Service;
import java.util.List;

/**
 * Created by Ross on 29/01/2018.
 */
public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        System.out.printf("%n%n Simply Retrieving What's there %n%n");
        fetchAllData().stream().forEach(System.out::println);
    }

    @SuppressWarnings("unchecked")
    private static List<Country> fetchAllData(){
        // Open a session
        Session session = sessionFactory.openSession();

        // Create criteria
        Criteria criteria = session.createCriteria(Country.class);

        // Get a list of contact objects according to criteria object
        List<Country> contacts = criteria.list();

        // Close the session
        session.close();

        return contacts;
    }
}
