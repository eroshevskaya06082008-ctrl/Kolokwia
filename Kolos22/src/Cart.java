import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<Product> basket = new ArrayList<>();
    public void addProduct(Product product, int amount) {
        for (int i = 0; i < amount; i++) {
            basket.add(product);
        }
    }

    public double getPrice(int year, int month) {
        double sum = 0;
        for (int i = 0; i < basket.size(); i++) {
            sum += basket.get(i).getPrice(year, month);
        }

        return sum;
    }

    public double getInflation(int year1, int month1, int year2, int month2) {
        // procent wartosc inflacji

        double price1 = getPrice(year1, month1);

        double price2 = getPrice(year2, month2);
        //month the amount of months
        int months = (year2 - year1) * 12 + month2 - month1;
        double influencja = (price2 - price1)/price1*100/months*12;
        return influencja;
    }
}
