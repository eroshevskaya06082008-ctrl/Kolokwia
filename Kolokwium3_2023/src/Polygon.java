import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private final List<Point> points;
    public Polygon(List<Point> points){
        this.points = new ArrayList<>();
    }
    public List<Point> getPoints(){
        return new ArrayList<>(points);
    }
    public boolean inside(Point point){
        int counter = 0;
        int n = points.size();
        for(int i = 0; i < n; i++){
            Point pa = points.get(i);
            Point pb = points.get((i + 1) % n); //(i + 1) % n łączy ostatni punkt z pierwszym
            Point p1 = pa;
            Point p2 = pb;

            if(p1.y > p2.y){
                Point temp = p1;
                p1 = p2;
                p2 = temp;
            }

            if(pa.y < point.y && point.y < pb.y){
                double d = pb.x - pa.x;
                double x;
                if(d ==0){
                    x = p1.x;
                } else {
                    double a = (pb.y - pa.y)/ d;
                    double b = pa.y - a*pa.x;
                    x = (point.y - b)/a;
                }
                if(x < point.x){
                    counter++;
                }
            }
        }
        return counter%2 != 0;

    }

}
