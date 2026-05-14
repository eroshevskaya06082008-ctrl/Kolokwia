import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Clock {
    public LocalTime time;
    private City city;

    public Clock(City city){
        this.city = city;
        this.time = LocalTime.of(0, 0, 0);
    }
    public void setCurrentTime() {
        this.time = LocalTime.now();
    }

    public void setTime(int hour, int min, int sec) {

        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                    "Godzina musi być w zakresie 0-23"
            );
        }

        if (min < 0 || min > 59) {
            throw new IllegalArgumentException(
                    "Minuty muszą być w zakresie 0-59"
            );
        }

        if (sec < 0 || sec > 59) {
            throw new IllegalArgumentException(
                    "Sekundy muszą być w zakresie 0-59"
            );
        }

        this.time = LocalTime.of(hour, min, sec);
    }

    public void setCity(City city) {

        if (city.timeZone > this.city.timeZone) {
            time.plusHours(this.city.timeZone - city.timeZone);
        } else {
            time.minusHours(city.timeZone - this.city.timeZone);
        }

        this.city = city;


    }

    public String toString(){
        return this.time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }



}
