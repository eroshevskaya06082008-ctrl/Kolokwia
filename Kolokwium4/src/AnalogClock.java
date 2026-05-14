import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

public class AnalogClock extends Clock{
    public LocalTime getTime() {
        return this.time;
    }

    public AnalogClock(City city) {
        super(city);
    }
    public String toSvg(City city) {
        // 1. Obliczamy czas dla danego miasta (używając strefy czasowej miasta)
        LocalTime timeInCity = this.getTime().plusHours(city.getTimezone());

        // 2. Budujemy treść SVG w pamięci (używając StringBuilder dla wydajności)
        StringBuilder sb = new StringBuilder();

        sb.append("<svg width=\"200\" height=\"200\" viewBox=\"-100 -100 200 200\" xmlns=\"http://www.w3.org/2000/svg\">");
        sb.append("<circle cx=\"0\" cy=\"0\" r=\"90\" fill=\"none\" stroke=\"black\" stroke-width=\"2\" />");
        sb.append("""
    <g text-anchor="middle">
        <text x="0" y="-80" dy="6">12</text>
        <text x="80" y="0" dy="4">3</text>
        <text x="0" y="80" dy="6">6</text>
        <text x="-80" y="0" dy="4">9</text>
    </g>""");

        // 3. Ustawiamy obliczony czas na wskazówkach i pobieramy ich kod SVG
        for (ClockHand clockHand : wskazowki) {
            clockHand.setTime(timeInCity); // Ustawiamy czas miasta, a nie 20:15!
            sb.append(clockHand.toSvg());
        }

        sb.append("</svg>");

        // 4. Zwracamy gotowy kod SVG jako String
        return sb.toString();
    }
    private final List<ClockHand> wskazowki = List.of(new HourHand(), new SecondHand(), new MinuteHand());

}
