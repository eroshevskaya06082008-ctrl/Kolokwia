import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;

public class City {
    String capital;
    int timeZone;
    String latitude;
    String longitude;
    public City(String capital, int timeZone, String latitude, String longitude){
        this.capital = capital;
        this.timeZone = timeZone;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public static City parseLine(String line) throws IOException {
        String[] parts = line.split(",");
        return new City(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]);
    }
    public String getName() {
        return this.capital;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public static Map<String, City> parseFile(String path) throws IOException {
        Map<String, City> cityMap = new HashMap<>();
        try(BufferedReader bf = new BufferedReader(new FileReader(path))){
            String line;
            bf.readLine();
            while((line = bf.readLine()) != null){
                City city = parseLine(line);
                cityMap.put(city.capital, city);
            }
        }

        return cityMap;
    }

    public LocalTime localMeanTime(LocalTime time, City city) {
        //360 - 24 za godzine 15
        // E -, W +
        //180 - 12 22.56 - x
        //12 * 60 = 720
        //1 stop za 4 minuty -> 240 sec
        double localTime;
        String[] part = city.longitude.split(" ");
        if(part[1] == "E") {
            localTime = Double.parseDouble(part[0]) * 240;
            time.minusSeconds((long)localTime);
        } else {
            localTime = Double.parseDouble(part[0]) * 240;
            time.plusSeconds((long)localTime);
        }
        return time;
    }
    public long getOffSetMagnitude() {
        String[] parts = this.longitude.split(" ");
        double degrees = Double.parseDouble(parts[1]);
        return (long)(degrees * 240);
    }

    public static void worstTimeZoneFit(List<City> cities){
        cities.sort(Comparator.comparingLong(City::getOffSetMagnitude).reversed());
        for(City c : cities){
            System.out.println(c.capital + "\n");
        }
    }

    public static void generateAnalogClocksSvg(List<City> city, AnalogClock clock) throws IOException {
        Path folder = Paths.get(clock.toString().replace(":", "_"));
        try{
            if(!Files.exists(folder)) {
                Files.createDirectory(folder);
            }
            for(City cit : city){
                String fileName = cit.getName().replaceAll("[^a-zA-Z0-9ąęćłńóśźżĄĘĆŁŃÓŚŹŻ]", "_") + ".svg";
                Path filePath = folder.resolve(fileName);

                String svgContent = clock.toSvg(cit);

                Files.writeString(filePath, svgContent);
                System.out.println("The folder with name: " + filePath + " was created");
            }

        } catch (IOException e){
            System.err.println("Folder wasnt created" + e.getMessage());
        }

    }


    public long getTimezone() {
        return this.timeZone;
    }
}
