import java.time.LocalTime;

public abstract class ClockHand {
    LocalTime time;
    public abstract void setTime(LocalTime time);
    public LocalTime getTime() {
        return this.time;
    }
    public abstract String toSvg();
}
