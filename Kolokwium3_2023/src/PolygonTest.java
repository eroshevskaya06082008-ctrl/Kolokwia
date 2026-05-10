import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
//Alternatywa w Main -->
public class PolygonTest {

    private Polygon createSquare() {
        List<Point> points = Arrays.asList(
                new Point(10, 10),
                new Point(10, 20),
                new Point(20, 20),
                new Point(20,10)
        );
        return new Polygon(points);
    }
    @Test
    public void testPointInsidePolygon(){
        Polygon poly = createSquare();
        Point p = new Point(10, 15);
        assertTrue("Punkt powinien lezec w wielokoncie", poly.inside(p));
    }
    @Test
    public void testPointUnderPolygon() {
        Polygon poly = createSquare();
        Point p = new Point(15, 30);
        assertFalse("Punkt powinien lezec na zewnantrz wielokatu", poly.inside(p));
    }
    @Test
    public void testPointToTheRightOfPolygon() {
        Polygon poly = createSquare();
        Point p = new Point(30, 15);
        assertFalse("Punkt powinien lezec na zewnantrz wielokatu", poly.inside(p));

    }

}
