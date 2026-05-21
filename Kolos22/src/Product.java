import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Product {
    private String name;
    public Product(String name){
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public abstract double getPrice(int year, int month);

    public abstract double getPrice(int year, int month, String province);

    private static List<Product> productList = new ArrayList<>();
    public static List<Product> getProductList() {
        return productList;
    }
    public static void clearProducts(){
        productList.clear();
    }
    public static void addProducts(Function<Path, ? extends Product> fromCsvFunction, Path directoryPath) {
        try {
            // Przechodzimy po wszystkich plikach w podanym katalogu
            try (var stream = Files.list(directoryPath)) {
                stream.filter(Files::isRegularFile) // bierzemy tylko zwykłe pliki (nie podkatalogi)
                        .forEach(filePath -> {
                            // Wywołujemy nasz obiekt funkcyjny dla każdej ścieżki pliku
                            Product p = fromCsvFunction.apply(filePath);
                            productList.add(p);
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas odczytu katalogu: " + directoryPath, e);
        }
    }
    public static String getProducts(String prefix) throws AmbigiousProductException, IndexOutOfBoundsException {
        List<Product> products = new ArrayList<>();
        List<String> helps = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            String help = productList.get(i).getName();
            if (help.contains(prefix)) {
                products.add(productList.get(i));
                helps.add(help);
            }

        }
        if (products.isEmpty()) {
            throw new IndexOutOfBoundsException();
        } else if (products.size() == 1) {
            return products.get(0).getName();
        }

        throw new AmbigiousProductException(helps);
    }
}
