import java.time.LocalTime;

public class MinuteHand extends ClockHand{
    private double angle;
    @Override
    public void setTime(LocalTime time) {
        double min  = time.getMinute();
        this.angle = min * 6;
    }

    @Override
    public LocalTime getTime() {
        return this.time;
    }

    @Override
    public String toSvg() {
        String napis = "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"-70\" stroke=\"black\" stroke-width=\"2\" transform=\"rotate(" + this.angle + ")\" />\n";
        return napis;
    }
}
