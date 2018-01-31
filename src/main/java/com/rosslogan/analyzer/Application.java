package com.rosslogan.analyzer;

import com.rosslogan.analyzer.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ross on 29/01/2018.
 */
public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final String COUNTRY_HEADER = String.format("%-32s", "Country");
    private static final String INTERNET_USERS_HEADER = String.format("%-18s", "Internet Users");
    private static final String LITERACY_HEADER = String.format("%-18s", "Literacy");
    private static final String RESULTS_HEADER = COUNTRY_HEADER + INTERNET_USERS_HEADER + LITERACY_HEADER + "%n";
    private static final String COLUMNS_RESULTS_SEPARATOR = String.format("%-66s", "-").replace(" ", "-") + "%n";


    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        System.out.printf(RESULTS_HEADER);
        System.out.printf(COLUMNS_RESULTS_SEPARATOR);
        fetchAllData().stream().forEach(System.out::println);

        List<Country> countryData = fetchAllData();

        Comparator<Country> internetUsers = Comparator.comparingDouble(Country::getInternetUsers);
        Comparator<Country> adultLiteracyRate = Comparator.comparingDouble(Country::getAdultLiteracyRate);

        List<Country> validInternetUsers = countryData.stream().filter(country ->
                country.getInternetUsers()!=null).collect(Collectors.toList());
        List<Country> validAdultLiteracy = countryData.stream().filter(country ->
                country.getAdultLiteracyRate()!=null).collect(Collectors.toList());


        //Find min and max internet users
        Country minInternetUsers = validInternetUsers.stream().min(internetUsers).get();
        Country maxInternetUsers = validInternetUsers.stream().max(internetUsers).get();

        //Find min and max adult literacy rate
        Country minAdultLiteracyRate = validAdultLiteracy.stream().min(adultLiteracyRate).get();
        Country maxAdultLiteracyRate = validAdultLiteracy.stream().max(adultLiteracyRate).get();

        System.out.printf("%n%nStatistics Output%n%n");
        System.out.println("Min Internet Users: " + minInternetUsers.getName() +
                " " + minInternetUsers.getInternetUsers());
        System.out.println("Max Internet users: " + maxInternetUsers.getName() +
                " " + maxInternetUsers.getInternetUsers());
        System.out.println("Min Adult Literacy: " + minAdultLiteracyRate.getName() +
                " " + minAdultLiteracyRate.getAdultLiteracyRate());
        System.out.println("Max Adult Literacy: " + maxAdultLiteracyRate.getName() +
                " " + maxAdultLiteracyRate.getAdultLiteracyRate());

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
