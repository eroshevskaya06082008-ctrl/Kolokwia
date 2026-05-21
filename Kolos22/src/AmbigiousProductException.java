import java.util.List;

public class AmbigiousProductException extends RuntimeException {
    public AmbigiousProductException(List<String> list){
        super("zdublowane produkty: " + list.toString());

    }
}
