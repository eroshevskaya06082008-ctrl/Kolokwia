import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class City extends Polygon{
    public final Point center;
    public String name;
    private boolean port;

    Set<Resource.Type> resources = new HashSet<>();

    public City(double wallLength, String name, Point center, boolean port) {
        super(Arrays.asList(
                new Point(center.x - wallLength / 2, center.y - wallLength / 2),
                new Point(center.x + wallLength / 2, center.y - wallLength /2),
                new Point(center.x - wallLength / 2, center.y + wallLength/2 ),
                new Point(center.x + wallLength/2, center.y + wallLength / 2)
        ));
        this.center = center;
        this.name = name;
        this.port = false;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setPort(boolean port){
        this.port = port;
    }
    public boolean isPort(){
        return port;
    }

    //Krok 11.
    //Załóżmy bez sprawdzenia, że węgiel i drewno znajdują się na lądzie, a ryby w wodzie.
    //W klasie City utwórz zbiór (Set) obiektów Resource.Type o nazwie resources i dostępie pakietowym.
    //W tej samej klasie napisz metodę addResourcesInRange, która przyjmie listę obiektów Resource
    //oraz liczbę zmiennoprzecinkową range i umieści w zbiorze typy tych zasobów, które znajdują się
    //w odległości nie większej niż range od środka miasta. Ryby powinny być uwzględniane wyłącznie
    //w miastach portowych.
    public void addRecursesInRahge(List<Resource> resources, double range){
        for(Resource r : resources){
            double dx = this.center.x - r.position.x;
            double dy = this.center.y - r.position.y;
            double distance = Math.sqrt(dy*dy + dx*dx);
            if(distance <= range){
                if(r.type == Resource.Type.Fish){
                    if(this.port == true){
                        resources.add(r);
                    }
                } else {
                    resources.add(r);
                }
            }
        }
    }
    public String toString(){
        if(this.port == true){
            return this.name + "⚓";
        }
        return this.name;
    }

}
