import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapParser {
    static public final class Svg {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("rect")
        private List<Map<String, String>> rects = new ArrayList<>();
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("polygon")
        private List<Map<String, String>> polygons = new ArrayList<>();
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("text")
        private List<Map<String, String>> texts = new ArrayList<>();
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("circle")
        private List<Map<String, String>> circles = new ArrayList<>();
    }

    private record Label(Point point, String text) {}
    private List<Label> labels = new ArrayList<>();
    //krok 13
    private List<City> cities = new ArrayList<>();
    private List<Land> lands = new ArrayList<>();

    public List<Land> getLands(){
        return lands;
    }

    //krok 13
    private void parseText(Map<String, String> params) {
        String text = "";
        double x = Double.parseDouble(params.get("x"));
        double y = Double.parseDouble(params.get("y"));
        addLabel(text, new Point(x, y));
    }
    private void parsePolygon(Map<String, String> params) {
        String pointsRaw = params.get("points");
        if(pointsRaw == null){
            return;
        }
        List<Point> points = new ArrayList<>();
        String[] pairs = pointsRaw.trim().split("\\s+");
        for(String pair : pairs){
            String[] coords = pair.split(",");
            points.add(new Point(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
        }
        lands.add(new Land(points));
    }
    private void parseRect(Map<String, String> params) {
        double x = Double.parseDouble(params.get("x"));
        double y = Double.parseDouble(params.get("y"));
        double width =  Double.parseDouble(params.get("width"));

        Point center = new Point(x + width/2, y + width/2);
        cities.add(new City(width, null, center, true));


    }


    private void addLabel(String text, Point bottomLeft) {
        labels.add(new Label(bottomLeft, text));
    }


    void matchLabelsToTowns() {
        for(City city : cities){
            Label closestLabel = null;
            double mindistance = Double.MAX_VALUE;
            for(Label label : labels){
                double dx = city.center.x - label.point.x;
                double dy = city.center.y - label.point.y;
                double distance = Math.sqrt(dx*dx + dy*dy);
                if(distance < mindistance){
                    mindistance = distance;
                    closestLabel = label;
                }
            }
            if(closestLabel != null){
                city.setName(closestLabel.text());
            }
        }

    }

    void addCitiesToLands() {
        for(Land land : lands){
            for(City city : cities){
                if(land.inside(city.center)){
                    try{
                        land.addCity(city);
                    } catch (RuntimeException e){
                        System.err.println("Nie udalo sie dodac miasta: " + e.getMessage());
                    }
                }
            }
        }
    }
    //Krok 13.
    //Zapoznaj się z dołączoną klasą MapParser służącą do parsowania tego pliku. Klasa wykorzystuje
    //bibliotekę Jackson Dataformat XML. Dołącz ją do projektu. W klasie MapParser znajduje się lista
    //obiektów typu Label zawierającego reprezentację znaczników <text>. Metody parse i parseText
    //prezentują proces konwersji zawartości znacznika na obiekt.
    //W klasie MapParser stwórz analogiczne do istniejącej listy nazw, nowe listy lądów i miast i zapełnij je
    //wzorując się na istniejącym kodzie. Napisz publiczny akcesor do listy lądów.
    //Uwaga! Podczas wczytywania miasta jego nazwa nie jest znana. Można ją tymczasowo ustawić jako
    //null. Wartość x i y w znaczniku rect definiuje lewy, górny wierzchołek prostokąta. Pamiętaj
    //o obliczeniu pozycji środka miasta.
    //В 13-м шаге тебе нужно превратить «глупый» парсер, который просто
    // читает текст, в полноценный инструмент, который строит карту из
    // объектов Land (земля) и City (города), используя данные из
    // XML-файла (SVG).Вот подробный разбор того, что нужно сделать
    // технически:1. Добавление списков и аксораВ классе MapParser
    // сейчас есть только список labels. Тебе нужно:Создать private
    // List<Land> lands = new ArrayList<>();Создать private List<City>
    // cities = new ArrayList<>();Написать публичный метод getLands(),
    // который просто возвращает этот список.2. Парсинг полигонов
    // (<polygon>) — это твои LandВ SVG-файле земля описана тегом
    // polygon. У него есть атрибут points, который выглядит как строка:
    // "x1,y1 x2,y2 x3,y3 ...".Задача: Тебе нужно написать метод
    // (например, parsePolygon), который берет эту строку, режет её по
    // пробелам, потом по запятым, превращает координаты в double,
    // создает из них объекты Point и, наконец, создает объект Land.3.
    // Парсинг прямоугольников (<rect>) — это твои CityГорода в SVG —
    // это теги rect. У них есть параметры x, y и width (ширина).
    // Ловушка с координатами: В SVG x и y — это левый верхний угол.
    // А твой класс City (из Шага 6) требует в конструкторе центр
    // города.Формула: * $center.x = x + (width / 2)
    // $$center.y = y + (width / 2)$Имя города: В теге rect нет имени
    // города. Оно написано отдельно в теге text. Поэтому в этом шаге
    // при создании объекта City в поле имени передавай null.
    // Мы «поженим» имена с городами в следующем 14-м шаге.Как это
    // реализовать (пример логики):
    // Внутри метода parse(String path)
    // после того, как xmlMapper прочитал файл:Проход по svg.polygons:
    // Достаешь строку из item.get("points").Делаешь split(" ").Каждую
    // пару координат превращаешь в Point.Добавляешь новый Land в
    // список lands.Проход по svg.rects:Берешь x, y, width через
    // Double.parseDouble().Вычисляешь центр.Создаешь new City(null,
    // center, width).Добавляешь в список cities.

    void parse(String path) {
        XmlMapper xmlMapper = new XmlMapper();
        File file = new File(path);
        try {
            Svg svg = xmlMapper.readValue(file, Svg.class);
            if(svg.texts != null){
                for(var item : svg.texts){
                    parseText(item);
                }
            }
            if(svg.polygons != null){
                for(var item : svg.polygons){
                    parsePolygon(item);
                }
            }
            if(svg.rects != null) {
                for(var item : svg.rects){
                    parseRect(item);
                }
            }
            matchLabelsToTowns();
            addCitiesToLands();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
