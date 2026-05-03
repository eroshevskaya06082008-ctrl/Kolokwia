import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Krok 2.
//Napisz klasę FoodProduct dziedziczącą po Product. Stwórz w niej statyczną, publiczną metodę
//wytwórczą, analogiczną do tej istniejącej w NonFoodProduct:
//FoodProduct fromCsv(Path path),
//działającej tak, aby możliwe było wywołanie opisanych w dalszej części tego kroku metod.
//Klasa powinna posiadać publiczną metodę:
//double getPrice(int year, int month, String province).
//Metoda ma zwracać cenę w określonym województwie przekazanym napisem składającym się z
//wielkich liter (jak w plikach z danymi). Jeżeli zostanie podany napis nie pasujący do żadnego
//województwa lub data będzie niewłaściwa, należy rzucić wyjątek IndexOutOfBoundsException.
//Klasa powinna także nadpisywać metodę:
//double getPrice(int year, int month)
//w taki sposób, że jako wynik będzie zwracana średnia arytmetyczna cen ze wszystkich
//województw.
//Przetestuj działanie dwu- i trójargumentowej metody FoodProduct::getPrice w metodzie
//Main::main.
public class FoodProduct extends Product{
    private final Map<String,Map<Integer, Double>> provincePrices = new HashMap<>();
    public FoodProduct(String name){
        super(name);
    }
    public static FoodProduct fromCsv(Path path) {
        try (Scanner scanner = new Scanner(path)) {
            if (!scanner.hasNextLine()) {
                return null;
            }
            String name = scanner.nextLine();
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            FoodProduct product = new FoodProduct(name);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                String province = parts[0];
                Map<Integer, Double> prices = new HashMap<>();
                int startYear = 2010;
                int startMonth = 1;
                for (int i = 1; i < parts.length; i++) {
                    double price = Double.parseDouble(parts[i].replace(",", "."));
                    prices.put(startYear * 100 + startMonth, price);
                    startMonth++;
                    if (startMonth > 12) {
                        startMonth = 1;
                        startYear++;
                    }
                }
                product.provincePrices.put(province, prices);
            }
            return product;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Blad odczytu " + e.getMessage());
        }
    }
    public double getPrice(int year, int month, String province){
        validateDate(year, month);
        if(!provincePrices.containsKey(province)){
            throw new IndexOutOfBoundsException("nie znaleziono wojewodstwa");
        }
        Integer dateKey = year * 100 + month;
        Map<Integer, Double> prices = provincePrices.get(province);
        if(!prices.containsKey(dateKey)){
            return 0.0;
        }
        return prices.get(dateKey);
    }
    @Override
    public double getPrice(int year, int month){
        validateDate(year, month);
        double sum = 0;
        int count = 0;
        for(String name : provincePrices.keySet()){
            sum += getPrice(year, month, name);
            count++;
        }
        if(count == 0){
            return 0.0;
        }
        return sum/count;
    }
}