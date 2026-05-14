import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main{
  public static void main(String[] args){
    Path filePath = Paths.get("kolokwium2/src/nonfood");
    NonFoodProduct product = NonFoodProduct.fromCsv(filePath);
    if(product != null){
      System.out.println("Product: " + product.getName());
      try{
        int testyear = 2015;
        int testmonth = 6;
        double price = product.getPrice(testyear, testmonth);
        System.out.println(testyear + " " + testmonth + " " + price);
      } catch(IndexOutOfBoundsException e){
        System.out.println("Blad: "+ e.getMessage());
      }
      try {
        System.out.print("Test błędnego miesiąca (13): ");
        product.getPrice(2020, 13);
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Złapano wyjątek: " + e.getMessage());
      }

      // 5. Тестируем дату вне диапазона данных (после марта 2022)
      try {
        System.out.print("Test daty poza zakresem (04.2022): ");
        product.getPrice(2022, 4);
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Złapano wyjątek: " + e.getMessage());
      }
    } else {
      System.out.println("nie udalo sie wczytac informacji");
    }
    FoodProduct onion = FoodProduct.fromCsv(Paths.get("src/food/cebula.csv"));
    if(onion != null){
      System.out.println("Produkt spozywczy: " + onion.getName());
      try{
        double priceInMazowieckie = onion.getPrice(2022, 3, "MAZOWIECKIE");
        System.out.println("cena w MAZOWIECKIE: "+ priceInMazowieckie);
        double avgPrice = onion.getPrice(2015, 4);
        System.out.println("Avg price: " + avgPrice);
        onion.getPrice(2025, 1, "NIBYLANDIA");
      } catch(IndexOutOfBoundsException e){
        System.out.println("Blad odczytu: " + e.getMessage());
      }
    }
    Product.clearProducts();
    Product.addProducts(NonFoodProduct::fromCsv, Paths.get("src/nonfood"));
    Product.addProducts(FoodProduct::fromCsv, Paths.get("src/food"));
    System.out.println("zaladowano lacznie productow: " + Product.getProducts().size());
    for(Product p : Product.getProducts()){
      try{
        System.out.println("Product:" + p.getName() + " Srednia 01.2022: " + p.getPrice(2022, 1));
      } catch (Exception e){
        e.getMessage();
      }
    }
    String[] testPrefixes = {"Abc", "Buraki", "Ja"};
    for(String prefix : testPrefixes){
      try{
        Product p = Product.getProduct(prefix);
        System.out.println("Znalieziono: " + p.getName());
      } catch (IndexOutOfBoundsException e){
        System.out.println("Znaleziono 0 wynikow: " + e.getMessage());
      } catch(AmbigiousProductException e){
        System.out.println("Znaleziono wiecej niz 1 wynik: " + e.getMessage());
      }
    }
    Cart myCart = new Cart();
    try{
      Product p1 = Product.getProduct("Cebula");
      Product p2 = Product.getProduct("Zelazko");

      myCart.addProducct(p1, 5);
      myCart.addProducct(p2, 1);
      double v1 = myCart.getPrice(2010, 1);
      double v2 = myCart.getPrice(2022, 3);

      System.out.println("Wartosc koszyka w 01.2010: " + v1);
      System.out.println("Wartosc koszyka w 03.2022: " + v2);
      double inflation = myCart.getInflation(2010, 1, 2022, 3);
      System.out.println("Inflacja: " + inflation);
    } catch (Exception e){
      System.out.println("Blad podczas operacji na koszyku: " + e.getMessage());
    }
  }
}