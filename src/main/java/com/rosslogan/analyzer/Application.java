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

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final String COUNTRY_HEADER = String.format("%-32s", "Country");
    private static final String INTERNET_USERS_HEADER = String.format("%-18s", "Internet Users");
    private static final String LITERACY_HEADER = String.format("%-18s", "Literacy");
    private static final String RESULTS_HEADER = COUNTRY_HEADER + INTERNET_USERS_HEADER + LITERACY_HEADER + "%n";
    private static final String COLUMNS_RESULTS_SEPARATOR = String.format("%-66s", "-").replace(" ", "-") + "%n";
    private static final String STATISTICS_MIN_INTERNET_USERS_LABEL = String.format("%-26s", "Min Internet Users:");
    private static final String STATISTICS_MAX_INTERNET_USERS_LABEL = String.format("%-26s", "Max Internet users:");
    private static final String STATISTICS_MIN_ADULT_LITERACY_LABEL = String.format("%-26s", "Min Adult Literacy:");
    private static final String STATISTICS_MAX_ADULT_LITERACY_LABEL = String.format("%-26s", "Max Adult Literacy:");
    private static final String STATISTICS_CORRELATION_LABEL = String.format("%-26s", "Correlation Coefficient:");
    private static final Comparator<Country> COMPARE_INTERNET_USERS =
            Comparator.comparingDouble(Country::getInternetUsers);
    private static final Comparator<Country> COMPARE_ADULT_LITERACY =
            Comparator.comparingDouble(Country::getAdultLiteracyRate);


    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {

        String selection="";
        Scanner scanner = new Scanner(System.in);

        while(!selection.equals("Q")){
            displayUserSelectionOptions();
            selection = scanner.next();
            switch (selection) {
                case "A": displayAllCountryData(fetchAllData());
                          break;
                case "B": displayAllCountryStatistics(fetchAllData());
                          break;
                case "Q": System.out.printf("%nExiting");
                          System.exit(0);
                default : System.out.printf("%n Invalid option selected, please try again!");
                          break;
            }
        }
    }

    private static void displayUserSelectionOptions() {
        System.out.printf("%n%nPlease select an option from the below by " +
                "entering the letter for the option you are requesting: %n%n");
        System.out.println("(A) - Display All Country Data");
        System.out.println("(B) - Display Statistics");
        System.out.println("(Q) - Quit");
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
        System.out.println(STATISTICS_MIN_INTERNET_USERS_LABEL + String.format("%-32s", minInternetUsers.getName()) +
                String.format("%.02f", Math.round(minInternetUsers.getInternetUsers() * 100.0)/100.0));
        System.out.println(STATISTICS_MAX_INTERNET_USERS_LABEL + String.format("%-32s", maxInternetUsers.getName()) +
                String.format("%.02f", Math.round(maxInternetUsers.getInternetUsers() * 100.0)/100.0));
        System.out.println(STATISTICS_MIN_ADULT_LITERACY_LABEL + String.format("%-32s", minAdultLiteracyRate.getName()) +
                String.format("%.02f", Math.round(minAdultLiteracyRate.getInternetUsers() * 100.0)/100.0));
        System.out.println(STATISTICS_MAX_ADULT_LITERACY_LABEL + String.format("%-32s", maxAdultLiteracyRate.getName()) +
                String.format("%.02f", Math.round(maxAdultLiteracyRate.getInternetUsers() * 100.0)/100.0));

        List<Country> validIndicators = countryData.stream().filter(country -> country.getInternetUsers()!=null)
                .filter(country -> country.getAdultLiteracyRate()!=null).collect(Collectors.toList());

        double correlationCoefficient = calculateCorrelationCoefficient(validIndicators);
        System.out.printf("%n" + STATISTICS_CORRELATION_LABEL + correlationCoefficient);

    }

    private static double calculateCorrelationCoefficient(List<Country> countryData){
        Double internetUserMean = countryData.stream().mapToDouble(c -> c.getInternetUsers()).average().getAsDouble();
        List<Double> internetUserValues = countryData.stream().map(Country::getInternetUsers)
                .collect(Collectors.toList());
        Double internetUserSD = calculateStandardDeviation(internetUserValues, internetUserMean);

        Double adultLiteracyMean = countryData.stream().mapToDouble(c -> c.getAdultLiteracyRate())
                .average().getAsDouble();
        List<Double> adultLiteracyValues = countryData.stream().map(Country::getAdultLiteracyRate)
                .collect(Collectors.toList());
        Double adultLiteracySD = calculateStandardDeviation(adultLiteracyValues, adultLiteracyMean);

        double totalStandardizedValues = 0;
        for(Country country: countryData){
            double x = (country.getInternetUsers() - internetUserMean)/internetUserSD;
            double y = (country.getAdultLiteracyRate() - adultLiteracyMean)/adultLiteracySD;
            totalStandardizedValues = totalStandardizedValues + (x*y);
        }

        return totalStandardizedValues/(countryData.size()-1);

    }

    private static double calculateStandardDeviation(List<Double> data, double mean){
        double total = 0;
        for(Double value: data){
            total = total + Math.pow((value - mean), 2);
        }
        return Math.sqrt((total/(data.size()-1)));
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
