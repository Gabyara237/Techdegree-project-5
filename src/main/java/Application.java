import com.teamtreehouse.worldbank.model.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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
        fetchAllCountries();
    }

    @SuppressWarnings("unchecked")
    private static void fetchAllCountries() {
        String adultLiteracyRate ="";
        String internetUser ="";

        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Country> criteriaQuery = builder.createQuery(Country.class);
            Root<Country> root = criteriaQuery.from(Country.class);

            criteriaQuery.select(root);

            List<Country> countries = session.createQuery(criteriaQuery).getResultList();

            System.out.printf("%-15s %-30s %20s %17s\n", "Code", "Name" , "Internet User","Literacy");
            System.out.println("------------------------------------------------------------------------------------------");

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

                System.out.printf("%-10s %-30s %20s %20s\n", country.getCode(),country.getName(),internetUser, adultLiteracyRate);

            }

        }
    }


}
