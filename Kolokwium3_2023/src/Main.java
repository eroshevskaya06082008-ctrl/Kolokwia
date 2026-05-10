import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class Main{
  public static void main(String[] args){
      List<Point> vertices = Arrays.asList(
              new Point(10, 10),
              new Point(10, 20),
              new Point(20, 10),
              new Point(20, 20)

      );

      List<Resource> resources = List.of(
              new Resource(new Point(50, 50), Resource.Type.Wood),
              new Resource(new Point(10, 20), Resource.Type.Fish)
      );

      Polygon poly = new Polygon(vertices);

      Point p1 = new Point(15, 15);
      System.out.println("Punkt znajduje sie na wewnatrz wielokatu"+ poly.inside(p1));

      Point p2 = new Point(15, 30);
      System.out.println("Punkt znajduje sie na zewnatrz wielokatu"+ poly.inside(p2));

      Point p3 = new Point(30, 15);
      System.out.println("Punkt znajduje sie na zewnatrz wielokatu"+ poly.inside(p3));

      Land land = new Land(Arrays.asList(
              new Point(0, 0),
              new Point(100, 0),
              new Point(0, 100),
              new Point(100, 100)
      ));

      City coastalCity = new City(10, "Gdansk", new Point(95, 50), false);
      coastalCity.addRecursesInRahge(resources, 5.0);

      land.addCity(coastalCity);

      assertTrue("There is a port in the city", coastalCity.isPort());

      MapParser parser = new MapParser();
      parser.parse("map.svg");
      for(Land land1 : parser.getLands()) {
          System.out.println(land1.toString());
      }
  }
}
