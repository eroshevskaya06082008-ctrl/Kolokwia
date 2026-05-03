import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Product {
    private final String name;

    public Product(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    protected void validateDate(int year, int month){
        if(month < 1 || month > 12){
            throw new IndexOutOfBoundsException();
        }
        if((year < 2010 || year > 2022) || (year == 2022 && month > 3)){
            throw new IndexOutOfBoundsException();
        }
    }

    public abstract double getPrice(int year, int month) throws IndexOutOfBoundsException;
    public abstract double getPrice(int year, int month, String province) throws IndexOutOfBoundsException;
    //Krok 3.
    //W klasie Product stwórz prywatną, statyczną listę obiektów klasy Product. Napisz statyczną,
    //publiczną metodę Product::clearProducts czyszczącą listę products oraz metodę
    //Product::addProducts dodającą do niej elementy, która przyjmie dwa parametry:
    //● obiekt funkcyjny, do którego można przypisać metody FoodProduct::fromCsv oraz
    //NonFoodProduct::fromCsv,
    //● obiekt Path zawierający ścieżkę do katalogu z plikami danych.
    //Metoda Product::addProducts powinna dodać do obiektu products obiekty utworzone na
    //podstawie plików z danymi.
    //W metodzie Main::main należy wywołać metodę Product::addProducts dwa razy: dla ścieżki
    //“data/nonfood” i metody NonFoodProduct::fromCsv oraz dla ścieżki “data/food” i metody
    //FoodProduct::fromCsv.
    private static List<Product> products = new ArrayList<>();
    public static void clearProducts(){
        products.clear();
    }
    public static void addProducts(Function<Path, ? extends Product> factory, Path path){
        try(Stream<Path> paths = Files.list(path)){
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> {
                        Product product = factory.apply(p);
                        if(product != null){
                            products.add(product);
                        }
                    });
        } catch (IOException e){
            System.err.println("Blad odczytu katalogu: " + e.getMessage());
        }

    }
    public static List<Product> getProducts(){
        return new ArrayList<>(products);
    }

    public static Product getProduct(String prefix) throws IndexOutOfBoundsException{
        List<Product> matches = products.stream()
                .filter(p -> p.getName().startsWith(prefix))
                .collect(Collectors.toList());
        if(matches.isEmpty()){
            throw new IndexOutOfBoundsException("Nie znaleziono produktu o prefiksie " + prefix);
        }
        if(matches.size() == 1){
            return matches.get(0);
        }

        List<String> names = matches.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
        throw new AmbigiousProductException(names);

    }
}
