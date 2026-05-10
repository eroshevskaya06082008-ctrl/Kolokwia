import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class CityTest {

    public class TestData{
        City city;
        Resource resource;
        boolean expected;

        public TestData(City city, Resource resource, boolean expected){
            this.city = city;
            this.resource = resource;
            this.expected = expected;
        }
    }

    //Krok 12.
    //Napisz klasę CityTest zawierającą sparametryzowany test zawierający:
    //- poprawne dodanie węgla,
    //- nieudaną próbę dodania drewna spoza zasięgu miasta,
    //- poprawne dodanie ryb do miasta portowego,
    //- nieudaną próbę dodania ryb do miasta nieportowego.
    //Test będzie wymagał definicji miasta śródlądowego i morskiego, co można wykonać na wiele
    //sposobów, np. przez ustawienie ich jako pola klasy.
    //Test powinien przyjmować argumenty: miasto, zasób, oczekiwana wartość logiczna.
    //Sposób parametryzacji testu jest dowolny. Test, przy użyciu pakietowego dostępu do pola
    //City.resources powinien sprawdzać, czy testowany zasób został dodany do zbioru.
    //Dany jest plik map.svg zawierający przykładową mapę. Mapa zawiera niebieski prostokąt
    //reprezentujący wodę, który można pominąć w dalszych rozważaniach oraz za pomocą znaczników:
    //- polygon o kolorze zielonym - ląd,
    //- rect o kolorze czerwonym - miasto,
    //- circle o kolorze czarnym, brązowym, jasnoniebieskim - zasoby, odpowiednio węgiel, drewno,
    //ryby.
    //- text - nazwy miast.
    //Symbole zasobów nie będą rozważane w dalszej części zadania.
    @Test
    void manualResourceTest(){
        City inland = new City(10, "Warszawa", new Point(100, 100), false);
        City port = new City(10, "Gdansk", new Point(0, 0), true);

        TestData[] cases = {
                new TestData(inland, new Resource(new Point(105, 105), Resource.Type.Coal), true),
                new TestData(port, new Resource(new Point(200, 200), Resource.Type.Wood), false),
                new TestData(port, new Resource(new Point(5, 5), Resource.Type.Fish), true),
                new TestData(inland, new Resource(new Point(105, 105), Resource.Type.Fish), false)
        };
        for(TestData td : cases){
            td.city.resources.clear();
            td.city.addRecursesInRahge(Collections.singletonList(td.resource), 20.0);
            boolean actual = td.city.resources.contains(td.resource.type);
            if (td.expected) {
                assertTrue(actual, "Error " + td.resource.type + " for city: " + td.city.getName());
            } else {
                assertFalse(actual, "Error " + td.resource.type + " for city: " + td.city.getName());
            }
        }
    }
}
