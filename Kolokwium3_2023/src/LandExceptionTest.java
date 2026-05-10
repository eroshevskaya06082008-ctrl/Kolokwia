import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

public class LandExceptionTest {
    @Test
    public void testAddCity() {
        Land island = new Land(Arrays.asList(
                new Point(0, 0),
                new Point(10, 0),
                new Point(0, 10),
                new Point(10, 10)
        ));
        String name = "Atlantyda";
        City farAwayCity = new City(5, name, new Point(50, 50), true);
        try{
            island.addCity(farAwayCity);
            fail("Powinien zostac rzucony RunTimeEception");
        } catch (RuntimeException e){
            assertEquals("Wiadomosc wyjatku powinna byc nazwa miasta",name, e.getMessage());
        }
    }
}
