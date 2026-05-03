import java.io.FileNotFoundException;
import java.time.LocalDate;

public class Main{
    public static void main(String[] args) {
        try{
            Country.setFiles("confirmed_cases.csv", "deaths.csv");
            Country Poland = Country.fromCsv("Poland");
            if(Poland != null) {
                LocalDate testdate = LocalDate.of(2020, 2, 14);

                System.out.println("country was found: " + Poland.getName());
                System.out.println("Object type: " + Poland.getClass().getSimpleName());
            }
            Country australia = Country.fromCsv("Australia");
            if(australia != null) {
                System.out.println("country was found: " + Poland.getName());
                System.out.println("Object type: " + Poland.getClass().getSimpleName());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CountryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
