import java.util.List;

public class AmbigiousProductException extends RuntimeException {
    private final List<String> products;
    public AmbigiousProductException(List<String> products) {
        super("Znaleziono wiecej niz jeden product pasujacy do wzorca: " + products.toString());
        this.products = products;
    }
    public List<String> getConflictingProducts(){
        return products;
    }
}
