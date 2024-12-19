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

        Scanner scanner = new Scanner(System.in);
        int option =0;
        while(option != 6) {
            System.out.printf("%n%n --------  Menu --------   %n%n");
            System.out.println("1. Display existing countries.");
            System.out.println("2. Edit country.");
            System.out.println("3. Add country.");
            System.out.println("4. Delete country.");
            System.out.println("5. Display data analysis.");
            System.out.println("6. Exit.");

            if (scanner.hasNextInt()){
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option){
                    case 1:
                        System.out.println("Selected option: Display existing countries.");
                        System.out.printf("%n%nList of countries: %n%n");
                        displayAllCountries();
                        break;
                    case 2:
                        System.out.println("Selected option: Edit country.");
                        editCountry();
                        break;
                    case 3:
                        System.out.println("Selected option: Add country.");
                        createCountry();
                        break;
                    case 4:
                        System.out.println("Selected option: Delete country.");
                        deleteCountry();
                        break;
                    case 5:
                        System.out.println("Selected option: Display data analysis.");
                        dataAnalysis();
                        break;
                    case 6:
                        System.out.println("Selected option: Exit.");
                        break;

                }
            }
        }
    }



    private static void deleteCountry() {
        Scanner scanner = new Scanner(System.in);
        String countryCode="";
        String confirmAnswer;
        Country country;
        while(true) {
            System.out.println("Please enter the country code of the country you want to delete, it must be 3 characters long.");
            countryCode = scanner.nextLine().toUpperCase().trim();

            if(countryCode.length() == 3 ){

                country = findCountryByCode(countryCode);
                if(country!= null){
                    while(true) {
                        System.out.printf("Are you sure you want to delete the country %s with the code %s?. Type Y for 'Yes', and N for 'No'%n", country.getName(), country.getCode());
                        confirmAnswer = scanner.nextLine().toUpperCase().trim();
                        if (confirmAnswer.equals("Y")) {
                            delete(country);
                            System.out.printf("%nThank you for your confirmation, the country %s has been successfully deleted with the code %s.", country.getName(), country.getCode());

                            break;
                        } else if (confirmAnswer.equals("N")) {
                            System.out.println("%nThank you for your confirmation, we will not proceed with the elimination of the country.");
                            break;
                        } else {
                            System.out.println("%nInvalid Input.");
                        }
                    }
                    break;

                }else{
                    System.out.println("Country code not found in the database.");
                }

            }else{
                System.out.println("Invalid input. The code must be 3 letters.");
            }
        }

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
            if(scanner.hasNextDouble()) {
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
        System.out.println("Country successfully added");
        System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name", "Internet User", "Adult Literacy Rate");
        System.out.println("----------------------------------------------------------------------------------------------");
        printCountry(country);
    }


    private static void editCountry() {
        Scanner scanner = new Scanner(System.in);
        String countryCode;
        Country country;
        String input;
        String newName;
        Double newInternetUsers;
        Double newAdultLiteracyRate;

        while(true) {
            System.out.println("Please enter the country code you want to edit");
            countryCode = scanner.nextLine().toUpperCase();

            country = findCountryByCode(countryCode);

            if (country != null) {
                System.out.printf("%nCountry you want to edit:%n%n");
                System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name", "Internet User", "Adult Literacy Rate");
                System.out.println("----------------------------------------------------------------------------------------------");
                printCountry(country);

                while (true) {
                    System.out.printf("%nDo you want to edit the country name? Type Y for 'Yes', and N for 'No'. %n");
                    input = scanner.nextLine().toUpperCase().trim();
                    if (input.equals("Y")) {
                        while (true) {
                            System.out.println("Please enter the name of the edited country");
                            newName = scanner.nextLine();
                            if (newName.isEmpty() || newName.isBlank()) {
                                System.out.println("Invalid input. The name cannot be empty or contain only whitespace.");
                            } else {
                                country.setName(newName);
                                break;
                            }
                        }
                        break;
                    } else if (input.equals("N")) {
                        break;
                    }else{
                        System.out.println("Incorrect option selected.");
                    }
                }

                while (true) {
                    System.out.printf("%nDo you want to edit the internet users field? Type Y for 'Yes', and N for 'No'. %n");
                    input = scanner.nextLine().toUpperCase().trim();
                    if (input.equals("Y")) {
                        System.out.println("Please enter the edited value for internet users");
                        newInternetUsers = scanner.nextDouble();
                        scanner.nextLine();
                        country.setInternetUsers(newInternetUsers);
                        break;
                    } else if (input.equals("N")) {
                        break;
                    }else{
                        System.out.println("Incorrect option selected.");
                    }
                }

                while (true) {
                    System.out.println("Do you want to edit the adult literacy rate field? Type Y for 'Yes', and N for 'No'");
                    input = scanner.nextLine().toUpperCase().trim();
                    if (input.equals("Y")) {
                        System.out.println("Please enter the edited value for adult literacy rate.");
                        newAdultLiteracyRate = scanner.nextDouble();
                        scanner.nextLine();
                        country.setAdultLiteracyRate(newAdultLiteracyRate);
                        break;
                    } else if (input.equals("N")) {
                        break;
                    }else{
                        System.out.println("Incorrect option selected.");
                    }
                }

                update(country);

                System.out.println("Country successfully edited");
                System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name", "Internet User", "Adult Literacy Rate");
                System.out.println("----------------------------------------------------------------------------------------------");
                printCountry(country);

                break;
            } else {
                System.out.println("Incorrect country code.");
            }

        }
    }

    private static Country findCountryByCode(String countryCode) {

        Session session = sessionFactory.openSession();

        Country country = session.get(Country.class,countryCode);

        session.close();

        return country;
    }

    private static void delete(Country country) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.delete(country);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
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

        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Country> criteriaQuery = builder.createQuery(Country.class);
            Root<Country> root = criteriaQuery.from(Country.class);

            criteriaQuery.select(root);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    private static void displayAllCountries(){

        List<Country> countries= fetchAllCountries();
        System.out.printf("%-15s %-30s %20s %25s\n", "Code", "Name" , "Internet User","Adult Literacy Rate");
        System.out.println("----------------------------------------------------------------------------------------------");

        for(Country country : countries){

            printCountry(country);
        }
    }

    private static void dataAnalysis() {
        List<Country> countries = fetchAllCountries();


        Optional<Country> maxInternetUserCountry = countries.stream()
                .filter(country -> country.getInternetUsers() !=null)
                .max(Comparator.comparing(Country::getInternetUsers));

        Optional<Country> minInternetUserCountry = countries.stream()
                .filter(country -> country.getInternetUsers() !=null)
                .min(Comparator.comparing(Country::getInternetUsers));


        Optional<Country> maxAdultLiteracyRate = countries.stream()
                .filter(country -> country.getAdultLiteracyRate() !=null)
                .max(Comparator.comparing(Country::getAdultLiteracyRate));

        Optional<Country> minAdultLiteracyRate = countries.stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .min(Comparator.comparing(Country::getAdultLiteracyRate));


        System.out.printf("\n\n%50s\n\n", "DATA ANALYSIS");
        System.out.printf("%-20s %22s %22s\n", "Indicator", "Maximum", "Minimum");
        System.out.println("----------------------------------------------------------------------------------------------");


        maxInternetUserCountry.ifPresent(maxCountry ->
                minInternetUserCountry.ifPresent(minCountry ->
                        System.out.printf("%-20s %25s %20s %n",
                                "Internet Users",
                                String.format("%.2f",maxCountry.getInternetUsers()) + " (" + maxCountry.getCode() + ")" ,
                                String.format("%.2f",minCountry.getInternetUsers())+" ("+ minCountry.getCode() + ")" )
                )
        );


        maxAdultLiteracyRate.ifPresent(maxCountry ->
                minAdultLiteracyRate.ifPresent(minCountry ->
                        System.out.printf("%-20s %25s %20s %n",
                                "Adult Literacy Rate",
                                String.format("%.2f",maxCountry.getAdultLiteracyRate())+" ("+ maxCountry.getCode()+")",
                                String.format("%.2f",minCountry.getAdultLiteracyRate())+" ("+ minCountry.getCode()+")" )

                )

        );


    }

}
