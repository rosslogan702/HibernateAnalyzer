package com.rosslogan.analyzer;

import com.rosslogan.analyzer.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.*;
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
    private static final Comparator<Country> COMPARE_INTERNET_USERS =
            Comparator.comparingDouble(Country::getInternetUsers);
    private static final Comparator<Country> COMPARE_ADULT_LITERACY =
            Comparator.comparingDouble(Country::getAdultLiteracyRate);


    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        List<Country> countryData = fetchAllData();

        displayAllCountryData(countryData);


        displayAllCountryStatistics(countryData);

    }

    private static Country getMinInternetUser(List<Country> countryData){
        return countryData.stream().min(COMPARE_INTERNET_USERS).get();
    }

    private static Country getMaxInternetUser(List<Country> countryData){
        return countryData.stream().max(COMPARE_INTERNET_USERS).get();
    }

    private static void displayAllCountryStatistics(List<Country> countryData) {
        List<Country> validInternetUsers = countryData.stream().filter(country ->
                country.getInternetUsers()!=null).collect(Collectors.toList());
        List<Country> validAdultLiteracy = countryData.stream().filter(country ->
                country.getAdultLiteracyRate()!=null).collect(Collectors.toList());


        //Find min and max internet users
        Country minInternetUsers = validInternetUsers.stream().min(COMPARE_INTERNET_USERS).get();
        Country maxInternetUsers = validInternetUsers.stream().max(COMPARE_INTERNET_USERS).get();

        //Find min and max adult literacy rate
        Country minAdultLiteracyRate = validAdultLiteracy.stream().min(COMPARE_ADULT_LITERACY).get();
        Country maxAdultLiteracyRate = validAdultLiteracy.stream().max(COMPARE_ADULT_LITERACY).get();

        System.out.printf("%n%nStatistics Output%n%n");
        System.out.println("Min Internet Users: " + minInternetUsers.getName() +
                " " + minInternetUsers.getInternetUsers());
        System.out.println("Max Internet users: " + maxInternetUsers.getName() +
                " " + maxInternetUsers.getInternetUsers());
        System.out.println("Min Adult Literacy: " + minAdultLiteracyRate.getName() +
                " " + minAdultLiteracyRate.getAdultLiteracyRate());
        System.out.println("Max Adult Literacy: " + maxAdultLiteracyRate.getName() +
                " " + maxAdultLiteracyRate.getAdultLiteracyRate());

        List<Country> validIndicators = countryData.stream().filter(country -> country.getInternetUsers()!=null)
                .filter(country -> country.getAdultLiteracyRate()!=null).collect(Collectors.toList());

        double correlationCoefficient = calculateCorrelationCoefficient(validIndicators);
        System.out.println("Correlation Coefficient: " + correlationCoefficient);

    }

    private static double calculateCorrelationCoefficient(List<Country> countryData){
        Double internetUserMean = countryData.stream().mapToDouble(c -> c.getInternetUsers()).average().getAsDouble();
        Double internetUserSD = calculateStandardDeviationInternetUser(countryData);

        Double adultLiteracyMean = countryData.stream().mapToDouble(c -> c.getAdultLiteracyRate())
                .average().getAsDouble();
        Double adultLiteracySD = calculateStandardDeviationAdultLiteracy(countryData);

        double totalStandardizedValues = 0;
        for(Country country: countryData){
            double x = (country.getInternetUsers() - internetUserMean)/internetUserSD;
            double y = (country.getAdultLiteracyRate() - adultLiteracyMean)/adultLiteracySD;
            totalStandardizedValues = totalStandardizedValues + (x*y);
        }

        return totalStandardizedValues/(countryData.size()-1);

    }

    private static double calculateStandardDeviationInternetUser(List<Country> countryData){
        Double internetUserMean = countryData.stream().mapToDouble(c -> c.getInternetUsers()).average().getAsDouble();

        double total = 0;
        for(Country country: countryData){
            total = total + Math.pow((country.getInternetUsers()- internetUserMean),2);
        }
        total = Math.sqrt((total/(countryData.size()-1)));
        return total;
    }

    private static double calculateStandardDeviationAdultLiteracy(List<Country> countryData){
        Double adultLiteracyMean = countryData.stream().mapToDouble(c -> c.getAdultLiteracyRate())
                .average().getAsDouble();

        double total = 0;
        for(Country country: countryData){
            total = total + Math.pow((country.getAdultLiteracyRate()- adultLiteracyMean),2);
        }
        total = Math.sqrt((total/(countryData.size() -1)));
        return total;
    }

    private static void displayAllCountryData(List<Country> countryData) {
        System.out.printf(RESULTS_HEADER);
        System.out.printf(COLUMNS_RESULTS_SEPARATOR);
        countryData.stream().forEach(System.out::println);
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
