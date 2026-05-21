import java.nio.file.Path;

public class Main{
    public static void main(String[] args){
        Path path = Path.of("Kolos22\\src\\nonfood\\mydlo.csv");
        NonFoodProduct mydlo = NonFoodProduct.fromCsv(path);
        System.out.println(mydlo.getPrice(2010, 6));
        path = Path.of("Kolos22\\src\\food\\ziemniaki.csv");
        FoodProduct foodProduct = FoodProduct.fromCsv(path);
        System.out.println("srednia arytmetyczna: "+ foodProduct.getPrice(2020, 4));
        System.out.println("po provincji: " + foodProduct.getPrice(2020, 2, "PODLASKIE"));

        Path nonFoodPath = Path.of("Kolos22\\src\\nonfood");
        Path foodPath = Path.of("Kolos22\\src\\food");

        //Product.clearProducts();
        Product.addProducts(NonFoodProduct::fromCsv, nonFoodPath);
        System.out.println("zaladowano produktow: " + Product.getProductList().size());
        //System.out.println(Product.getProductList());
        Product mydlo1 = Product.getProductList().get(16);
        System.out.println(Product.getProductList().get(16).getName());
        System.out.println(Product.getProducts("Mydło"));

        Cart cart1 = new Cart();
        cart1.addProduct(mydlo1, 3);
        System.out.println(cart1.getInflation(2019, 3, 2020, 4));

    }
}
