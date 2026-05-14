import java.time.LocalTime;

public class HourHand extends ClockHand{
    private double angle;
    @Override
    public void setTime(LocalTime time) {
        double hour  = time.getHour();
        this.angle = hour * 30;
    }

    @Override
    public LocalTime getTime() {
        return this.time;
    }

    @Override
    public String toSvg() {
        String napis = "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"-50\" stroke=\"black\" stroke-width=\"4\" transform=\"rotate(" + this.angle + ")\" />\n";
        return napis;
    }


}
