import java.time.LocalTime;

public class SecondHand extends ClockHand{
    private double angle;
    @Override
    public void setTime(LocalTime time) {
        int sec = time.getSecond();
        this.angle = sec * 6;
    }

    @Override
    public LocalTime getTime() {
        return this.time;
    }

    @Override
    public String toSvg() {
        String napis = "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"-80\" stroke=\"red\" stroke-width=\"1\" transform=\"rotate(" + this.angle + ")\" />\n";
        return napis;
    }



}
