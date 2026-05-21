import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FoodProduct extends Product{
    private String name;
    private Double[][] prices;
    private String[] provinces;

    public FoodProduct(String name, Double[][] prices, String[] provinces){
        super(name);
        this.prices = prices;
        this.provinces = provinces;
    }
    public Double[][] getPrices(){
        return this.prices;
    }
    public String[] getProvinces(){
        return this.provinces;
    }

    @Override
    public double getPrice(int year, int month) {
        double sum = 0;
        int count = 0;
        //hueta
        // mapa - dla kazdej province - array array sum - sumprov+= sum
        for(int i = 0; i< provinces.length; i++){
            sum += getPrice(year, month, provinces[i]);
            count++;
        }
        return sum/count;
    }

    @Override
    public double getPrice(int year, int month, String province) {
        List<String> list = List.of(provinces);
        int index = list.indexOf(province);
        if((month < 1 || month > 12) || (year < 2010 || year > 2022)){
            throw new IndexOutOfBoundsException();
        }
        if(year == 2022){
            if(month > 3){
                throw new IndexOutOfBoundsException();
            }
        }
        return prices[index][(year - 2010)*12 + (month-1)];
    }
    public static FoodProduct fromCsv(Path path) {

        int size;
        try{
            size = (int)Files.lines(path).count() - 2;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String name;
        Double[][] prices = new Double[size][];
        String[] provinces = new String[size];
        int counter = 0;
        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine(); // odczytuję pierwszą linię i zapisuję ją jako nazwa
            scanner.nextLine();  // pomijam drugą linię z nagłówkiem tabeli
            String line;
            while(scanner.hasNext()){
                line = scanner.nextLine();
                String[] parts = line.split(";");
                provinces[counter] = parts[0];
                prices[counter] = new Double[parts.length -1];
                for(int i = 1; i < parts.length; i++){
                    String cleanLine = parts[i].replace(",", ".");
                    prices[counter][i-1] = Double.parseDouble(cleanLine);
                }
                counter++;
            }
             scanner.close();

            return new FoodProduct(name, prices, provinces);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
