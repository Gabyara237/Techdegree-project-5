import com.teamtreehouse.worldbank.model.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Application {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    public static void main(String[] args) {

        // Display a list of countries
        System.out.printf("%n%nList of countries: %n%n");
        List<Country> countries = fetchAllCountries();
        dataAnalysis(countries);
    }


    @SuppressWarnings("unchecked")
    private static List<Country> fetchAllCountries() {
        String adultLiteracyRate ="";
        String internetUser ="";

        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Country> criteriaQuery = builder.createQuery(Country.class);
            Root<Country> root = criteriaQuery.from(Country.class);

            criteriaQuery.select(root);

            List<Country> countries = session.createQuery(criteriaQuery).getResultList();

            System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name" , "Internet User","Adult Literacy Rate");
            System.out.println("----------------------------------------------------------------------------------------------");

            for(Country country : countries){

                if (country.getAdultLiteracyRate() == null){
                    adultLiteracyRate = "--";
                }else{
                    adultLiteracyRate = String.format("%.2f",country.getAdultLiteracyRate());
                }

                if (country.getInternetUsers() == null){
                    internetUser = "--";
                }else{
                    internetUser = String.format("%.2f",country.getInternetUsers());
                }

                System.out.printf("%-10s %-30s %20s %25s\n", country.getCode(),country.getName(),internetUser, adultLiteracyRate);

            }
            return countries;
        }
    }

    private static void dataAnalysis(List<Country> countries) {
        List<Double> internetUsers = new ArrayList<>();
        List<Double> adultLiteracyRate = new ArrayList<>();
        Double maxInternetUserValue;
        Double minInternetUserValue;
        Double maxAdultLiteracyRate;
        Double minAdultLiteracyRate;
        for (Country country:countries){
            if(country.getInternetUsers() != null) {
                internetUsers.add(country.getInternetUsers());
            }
            if (country.getAdultLiteracyRate() != null) {
                adultLiteracyRate.add(country.getAdultLiteracyRate());
            }
        }

        maxInternetUserValue = Collections.max(internetUsers);
        minInternetUserValue = Collections.min(internetUsers);

        maxAdultLiteracyRate = Collections.max(adultLiteracyRate);
        minAdultLiteracyRate = Collections.min(adultLiteracyRate);

        System.out.printf("\n\n%50s\n\n", "DATA ANALYSIS");
        System.out.printf("%-20s %22s %22s\n", "Indicator", "Maximum", "Minimum");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %20s %20s\n", "Internet Users", String.format("%.2f",maxInternetUserValue), String.format("%.2f",minInternetUserValue) );
        System.out.printf("%-20s %20s %20s\n", "Adult Literacy Rate", String.format("%.2f",maxAdultLiteracyRate), String.format("%.2f",minAdultLiteracyRate) );


    }

}
