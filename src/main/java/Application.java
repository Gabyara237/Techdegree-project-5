import com.teamtreehouse.worldbank.model.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.*;


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
        editCountry();
        createCountry();
        System.out.printf("%n%nList of countries: %n%n");
        fetchAllCountries();
    }

    private static void createCountry(){
       Scanner scanner = new Scanner(System.in);
        String code;
        String nameCountry;
        Double internetUsers;
        Double adultLiteracyRate;


        while(true) {
            System.out.println("Please enter the country code, it must be 3 characters long.");
            code = scanner.nextLine().toUpperCase().trim();

            if(code.length() == 3 ){
                break;
            }else{
                System.out.println("Invalid input. The code must be 3 letters.");
            }
        }

        while(true) {
            System.out.println("Please enter the country name.");
            nameCountry = scanner.nextLine();

            if (!nameCountry.isEmpty()){
                break;
            }else{
                System.out.println("Invalid input. The name must not be empty.");
            }
        }

        while(true) {
            System.out.println("Please enter the value for the indicator internet users.");
            if(scanner.hasNextDouble()){
                internetUsers = scanner.nextDouble();
                scanner.nextLine();
                if(internetUsers > 0){
                    break;
                }else{
                    System.out.println("Invalid input. The value must be a positive number.");

                }
            }else{
                System.out.println("Invalid input. The input must be a numerical value.");

            }
        }
        while(true) {
            System.out.println("Please enter the value for adult literacy rate.");
            if(true) {
                adultLiteracyRate = scanner.nextDouble();
                scanner.nextLine();
                if(adultLiteracyRate > 0){
                    break;
                }else{
                    System.out.println("Invalid input. The value must be a positive number.");
                }
            }else{
                System.out.println("Invalid input. The input must be a numerical value.");
            }
        }
        Country country = new Country.CountryBuilder(code)
                .withName(nameCountry)
                .withInternetUsers(internetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();
        save(country);
    }


    private static void editCountry() {
        Scanner scanner = new Scanner(System.in);
        String countryCode;
        Country country;
        String input;
        String newName;
        Double newInternetUsers;
        Double newAdultLiteracyRate;

        System.out.println("Please enter the country code you want to edit");
        countryCode = scanner.nextLine().toUpperCase();

        country = findCountryByCode(countryCode);

        if (country!=null) {
            System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name", "Internet User", "Adult Literacy Rate");
            System.out.println("----------------------------------------------------------------------------------------------");
            printCountry(country);

            System.out.printf("%nDo you want to edit the country name? Type Y for 'Yes', and N for 'No'. %n");
            input = scanner.nextLine().toUpperCase();
            if (input.equals("Y")) {
                System.out.println("Please enter the name of the edited country");
                newName= scanner.nextLine();

                country.setName(newName);
            }

            System.out.printf("%nDo you want to edit the internet users field? Type Y for 'Yes', and N for 'No'. %n");
            input = scanner.nextLine().toUpperCase();
            if (input.equals("Y")) {
                System.out.println("Please enter the edited value for internet users");
                newInternetUsers= scanner.nextDouble();
                scanner.nextLine();
                country.setInternetUsers(newInternetUsers);
            }

            System.out.println("Do you want to edit the adult literacy rate field? Type Y for 'Yes', and N for 'No'");
            input = scanner.nextLine().toUpperCase();
            if (input.equals("Y")) {
                System.out.println("Please enter the edited value for adult literacy rate.");
                newAdultLiteracyRate= scanner.nextDouble();
                scanner.nextLine();
                country.setAdultLiteracyRate(newAdultLiteracyRate);
            }

            update(country);

            System.out.println("Country successfully edited");
            System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name", "Internet User", "Adult Literacy Rate");
            System.out.println("----------------------------------------------------------------------------------------------");
            printCountry(country);


        }else{
            System.out.printf("Incorrect country code, please try again.");
        }

    }

    private static Country findCountryByCode(String countryCode) {

        Session session = sessionFactory.openSession();

        Country country = session.get(Country.class,countryCode);

        session.close();

        return country;
    }

    private static void update(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.update(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    private static void save(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        session.save(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    private static void printCountry(Country country){
        String adultLiteracyRate ="";
        String internetUser ="";
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

                printCountry(country);
            }
            return countries;
        }
    }

    private static void dataAnalysis(List<Country> countries) {
        List<Double> internetUsers = new ArrayList<>();
        List<Double> adultLiteracyRate = new ArrayList<>();
        List<String> codeCountriesInternetUsers = new ArrayList<>();
        List<String> codeCountriesAdultLiteracyRate = new ArrayList<>();

        String codeCountryMaxInternetUsers;
        String codeCountryMinInternetUsers;

        String codeCountryMaxAdultLiteracyRate;
        String codeCountryMinAdultLiteracyRate;

        int indexMaxInternetUser;
        int indexMinInternetUser;
        int indexMaxAdultLiteracyRate;
        int indexMinAdultLiteracyRate;

        Double maxInternetUserValue;
        Double minInternetUserValue;
        Double maxAdultLiteracyRate;
        Double minAdultLiteracyRate;
        for (Country country:countries){
            if(country.getInternetUsers() != null) {
                internetUsers.add(country.getInternetUsers());
                codeCountriesInternetUsers.add(country.getCode());
            }
            if (country.getAdultLiteracyRate() != null) {
                adultLiteracyRate.add(country.getAdultLiteracyRate());
                codeCountriesAdultLiteracyRate.add(country.getCode());
            }
        }

        // We obtain the maximum value of the Internet User indicator and the associated country code.
        maxInternetUserValue = Collections.max(internetUsers);
        indexMaxInternetUser = internetUsers.indexOf(maxInternetUserValue);
        codeCountryMaxInternetUsers = codeCountriesInternetUsers.get(indexMaxInternetUser);

        // We obtain the minimum value of the Internet User indicator and the associated country code.
        minInternetUserValue = Collections.min(internetUsers);
        indexMinInternetUser = internetUsers.indexOf(minInternetUserValue);
        codeCountryMinInternetUsers = codeCountriesInternetUsers.get(indexMinInternetUser);


        // We obtain the maximum value of the Adult Literacy Rate indicator and the associated country code.
        maxAdultLiteracyRate = Collections.max(adultLiteracyRate);
        indexMaxAdultLiteracyRate = adultLiteracyRate.indexOf(maxAdultLiteracyRate);
        codeCountryMaxAdultLiteracyRate = codeCountriesAdultLiteracyRate.get(indexMaxAdultLiteracyRate);


        // We obtain the minimum value of the Adult Literacy Rate indicator and the associated country code.
        minAdultLiteracyRate = Collections.min(adultLiteracyRate);
        indexMinAdultLiteracyRate = adultLiteracyRate.indexOf(minAdultLiteracyRate);
        codeCountryMinAdultLiteracyRate = codeCountriesAdultLiteracyRate.get(indexMinAdultLiteracyRate);

        System.out.printf("\n\n%50s\n\n", "DATA ANALYSIS");
        System.out.printf("%-20s %22s %22s\n", "Indicator", "Maximum", "Minimum");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %25s %20s\n", "Internet Users", String.format("%.2f",maxInternetUserValue)+"("+codeCountryMaxInternetUsers+")", String.format("%.2f",minInternetUserValue)+"("+codeCountryMinInternetUsers+")" );
        System.out.printf("%-20s %25s %20s\n", "Adult Literacy Rate", String.format("%.2f",maxAdultLiteracyRate)+"("+codeCountryMaxAdultLiteracyRate+")", String.format("%.2f",minAdultLiteracyRate)+"("+codeCountryMinAdultLiteracyRate+")" );


    }

}
