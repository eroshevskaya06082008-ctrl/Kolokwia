import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Land extends Polygon{
    private final List<City> cities;
    public Land(List<Point> points) {
        super(points);
        this.cities = new ArrayList<>();
    }
//Krok 7.
//W klasie Land stwórz prywatną listę miast. Napisz metodę addCity(City), która doda miasto do tej
//listy. Miasto może zostać dodane wyłącznie jeżeli jego środek znajduje się na lądzie. W przeciwnym
//razie należy rzucić wyjątek RuntimeException, którego metoda getMessage() powinna wyświetlić
//nazwę miasta
    public void addCity(City city){
        if(this.inside(city.center)){
            cities.add(city);
            System.out.println("City: " + city.getName() + "znajduje sie na ladzie");
        } else {
            throw new RuntimeException(city.getName());
        }
    }

    public void checkIfPort(City city){
        for(Point p : city.getPoints()){
            if(this.inside(p)){
                city.setPort(true);
                return;
            }
        }
        city.setPort(false);
    }
    @Override
    public  String toString(){
        List<String> cityStrings = new ArrayList<>();
        for(City city : this.cities){
            cityStrings.add(city.toString());
        }
        return String.join(", ", cityStrings);
    }

}
