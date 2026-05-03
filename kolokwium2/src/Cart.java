import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Krok 5.
//
//Napisz klasę Cart posiadającą publiczne metody
//
//● void addProduct(Product product, int amount) - dodającą do koszyka produkt w liczbie
//
//sztuk określonej zmienną amount,
//
//● double getPrice(int year, int month) - zwracającą wartość koszyka w zł we wskazanym
//
//roku i miesiącu,
//
//● double getInflation(int year1, int month1, int year2, int month2) - zwraca procentową
//
//wartość inflacji w ujęciu rocznym między dwoma wskazanymi miesiącami na podstawie
//
//zawartości koszyka, zakładając, że y1, m1 < y2, m2. Należy ją wyliczyć według wzoru:
//
//(price2 - price1) / price1 * 100 / months * 12,
//
//gdzie price1 i price2 to wartości koszyków w dwóch wskazanych miesiącach, a months to
//
//liczba miesięcy dzieląca wskazane daty.
//
//W metodzie Main::main, przy użyciu metody Product::addProduct dodaj do koszyka kilka
//
//produktów i wywołaj na jego rzecz metody Cart::getPrice i Cart::getInflation.
public class Cart {
    private final Map<Product, Integer> items = new HashMap<>();
    public void addProducct(Product product, int amount){
        if(product != null){
            items.put(product, items.getOrDefault(product, 0) + amount);
        }
    }
    public double getPrice(int year, int month) {
        double totalPrice = 0;
        for(Map.Entry<Product, Integer> entry : items.entrySet()){
            totalPrice += entry.getKey().getPrice(year, month) * entry.getValue();
        }
        return totalPrice;
    }
    public double getInflation(int year1, int month1, int year2, int month2) {
        double price1 = getPrice(year1, month1);
        double price2 = getPrice(year2, month2);
        if(price1 == 0) {
            return 0;
        }
        int months = (year2 - year1) * 12 + (month2 - month1);
        if(months <= 0) return 0;
        return (price2 - price1)/price1 * 100 / months * 12;
    }
}
