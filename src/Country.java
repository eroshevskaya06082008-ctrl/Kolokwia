import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Country {
    private final String name;
    private static String filenameConfitmedCases;
    private static String filenameDeaths;

    public Country(String name) {
        this.name = name;
    }
    public abstract int getConfirmedCases(LocalDate date);
    public abstract int getDeaths(LocalDate date);
    public String getName() {
        return name;
    }

    //Krok 2.
    //W klasie Country zdefiniuj statyczne, prywatne pola zawierające ścieżkę do obu plików CSV. Napisz
    //statyczną metodę klasy Country o nazwie setFiles ustawiającą te dwa pliki na wartości swoich argumentów. Metoda ta powinna zweryfikować, czy pliki istnieją i można je odczytać. Jeżeli nie będzie to
    //1
    //możliwe, należy rzucić wyjątek FileNotFoundException podając mu jako argument konstruktora ścieżkę
    //do błędnego pliku.
    //W klasie Country napisz publiczną, statyczną metodę fromCsv, która przyjmie jako argument napis
    //zawierający nazwę kraju, a zwróci polimorficzny obiekt typu Country. Metoda fromCsv powinna otwierać
    //i zamykać pliki i może założyć, że ścieżki do nich są poprawne.
    public static void setFiles(String confirmedCases, String deaths) throws FileNotFoundException {
        File f1 = new File(confirmedCases);
        if(!f1.exists() || !f1.canRead()) {
            throw new FileNotFoundException(confirmedCases);
        }
        File f2 = new File(deaths);
        if(!f2.exists() || !f2.canRead()){
            throw new FileNotFoundException(deaths);
        }
        filenameConfitmedCases = confirmedCases;
        filenameDeaths = deaths;

    }
    //Krok 3.
    //Zdefiniuj klasę wyjątku CountryNotFoundException tak, aby niemożliwa była kompilacja bez przechwycenia go. Wyjątek powinien być napisany tak, aby wywołanie metody getMessage() zwróciło nazwę
    //nieznalezionego państwa.
    //Wewnątrz klasy Country zdefiniuj prywatną, statyczną klasę CountryColumns. Klasa powinna posiadać publiczne, ostateczne, całkowite pola firstColumnIndex, columnCount ustawiane przy pomocy
    //konstruktora.
    //W klasie Country zdefiniuj prywatną, statyczną metodę getCountryColumns, która otrzyma jako
    //parametry: napis będący pierwszym wierszem pliku CSV oraz napis zawierający poszukiwane państwo.
    //Metoda powinna zwrócić obiekt klasy CountryColumns zawierający informację o początkowej kolumnie
    //oraz liczbie kolumn poświęconej państwu. Jest to jednocześnie informacja, czy państwo posiada prowincje.
    //Wywołaj metodę getCountryColumns wewnątrz metody fromCsv i przekaż dalej rzucany przez nią
    //wyjątek CountryNotFoundException.
    private static class CountryColumns{
        public final int firstColumnIndex;
        public final int columnCount;

        public CountryColumns(int firstColumnIndex, int columnCount){
            this.firstColumnIndex = firstColumnIndex;
            this.columnCount = columnCount;
        }
    }
    private static CountryColumns getCountryColumns(String line1, String country) throws CountryNotFoundException {
        String[] columns = line1.split(";");
        int firstIndex = -1;
        int count = 0;

        for(int i = 0; i< columns.length; i++) {
            if(columns[i].equals(country)){
                if(firstIndex == -1) {
                    firstIndex = i;
                }
                count++;
            }
        }
        if(firstIndex == -1){
            throw new CountryNotFoundException(country);
        }
        return new CountryColumns(firstIndex, count);
    }

    //Krok 5.
    //W metodzie fromCsv w zależności od rodzaju państwa utwórz obiekt klasy CountryWithoutProvinces lub CountryWithProvinces. Następnie dla kolejnych linii z danymi liczbowymi wywołaj metodę
    //addDailyStatistic na rzecz:
    //• obiektu CountryWithoutProvinces, lub
    //• kolejnych komórek tablicy prowincji obiektu CountryWithProvinces.
    //Należy zapisać statystyki dla wszystkich dat znajdujących się pliku.
    public static Country fromCsv(String Countryname) throws CountryNotFoundException, FileNotFoundException {
        if(filenameDeaths == null || filenameConfitmedCases == null) {
            return null;
        }
        try(BufferedReader brConf = new BufferedReader(new FileReader(filenameConfitmedCases));
        BufferedReader brDeaths = new BufferedReader(new FileReader(filenameDeaths))) {
            String cLines1 = brConf.readLine();
            String cLines2 = brConf.readLine();
            brDeaths.readLine();
            brDeaths.readLine();
            if(cLines1 == null || cLines2 == null) {
                return null;
            }
            // 6. Wywołujemy getCountryColumns, aby znaleźć indeksy kolumn dla danego państwa
            // Jeśli państwa nie ma w pliku, rzucony zostanie wyjątek CountryNotFoundException
            CountryColumns columnsInfo = getCountryColumns(cLines1, Countryname);
            // 7. Dzielimy drugi wiersz na tablicę nazw prowincji przy pomocy średnika
            String[] provinceNames = cLines2.split(";");
            Country resultCountry;
            // 8. Logika decydująca o typie obiektu (Polimorfizm)
            // Jeśli państwo zajmuje 1 kolumnę i ma wpis "nan", tworzymy CountryWithoutProvinces
            if(columnsInfo.columnCount == 1 && provinceNames[columnsInfo.firstColumnIndex].equals("nan")){
                resultCountry = new CountryWithoutProvinces(Countryname);
            } else {
                // W przeciwnym razie tworzymy tablicę prowincji i obiekt CountryWithProvinces
                CountryWithoutProvinces[] provinceArr = new CountryWithoutProvinces[columnsInfo.columnCount];
                for(int i = 0; i< columnsInfo.columnCount; i++) {
                    // Każda prowincja jest traktowana jako obiekt przechowujący dane (CountryWithoutProvinces)
                    provinceArr[i] = new CountryWithoutProvinces(provinceNames[columnsInfo.firstColumnIndex + i]);
                }
                resultCountry = new CountryWithProvinces(Countryname, provinceArr);
            }
            // 9. Formater daty potrzebny do zamiany tekstu "M/d/yy" na obiekt LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy", Locale.US);
            // 10. Pętla czytająca dane linia po linii (każda linia to nowa data)
            String confline, deathsline;
            while((confline = brConf.readLine()) != null && (deathsline = brDeaths.readLine()) != null){
                String[] cData = confline.split(";");
                String[] dData = confline.split(";");
                // 11. Parsujemy datę z pierwszej kolumny (indeks 0)
                LocalDate date = LocalDate.parse(cData[0], formatter);
                // 12. Rozdzielanie danych liczbowych do odpowiednich obiektów
                if(resultCountry instanceof  CountryWithoutProvinces) {
                    // Jeśli to pojedyncze państwo, bierzemy dane z wyznaczonej kolumny i dodajemy do mapy
                    int cases = Integer.parseInt(cData[columnsInfo.firstColumnIndex]);
                    int deaths = Integer.parseInt(dData[columnsInfo.firstColumnIndex]);
                    ((CountryWithoutProvinces) resultCountry).addDailyStatistic(date, cases, deaths);
                } else {
                    // Jeśli ma prowincje, iterujemy po nich i przypisujemy dane do każdej z osobna
                    CountryWithProvinces cwp = (CountryWithProvinces) resultCountry;
                    for(int i = 0; i< columnsInfo.columnCount; i++) {
                        int cases = Integer.parseInt(cData[columnsInfo.firstColumnIndex + i]);
                        int deaths = Integer.parseInt(dData[columnsInfo.firstColumnIndex + i]);
                        // Rzutowanie na CountryWithoutProvinces pozwala użyć metody addDailyStatistic
                        ((CountryWithoutProvinces) cwp.provinces[i]).addDailyStatistic(date, cases, deaths);
                    }
                }
            }
            return resultCountry;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //Krok 4.
    //Samodzielnie zaprojektuj w klasie CountryWithoutProvinces zawartość pozwalającą zapisać ile danego dnia w tym państwie było zakażeń i zgonów. Klasa powinna umożliwiać zapisanie wielu takich
    //wpisów.
    //Zdefiniuj publiczną metodę addDailyStatistic, przyjmującą jako argumenty datę oraz dwie liczby
    //całkowite - zachorowania i zgony, która dodaje je do zaproponowanej struktury. Daty należy zapisywać
    //jako obiekty klasy LocalDate.
    static class CountryWithoutProvinces extends Country {
        public CountryWithoutProvinces(String name) {
            super(name);
        }
        private final Map<LocalDate, DailyData> dailystatistics = new HashMap<>();

        private static class DailyData{
            final int cases;
            final int deaths;

            public DailyData(int cases, int deaths) {
                this.cases = cases;
                this.deaths = deaths;
            }
        }
        public void addDailyStatistic(LocalDate date, int cases, int deaths) {
            dailystatistics.put(date, new DailyData(cases, deaths));
        }
        public int getCasesAtDate(LocalDate date) {
            return dailystatistics.containsKey(date) ? dailystatistics.get(date).cases : 0;
        }
        public int getDeathsAtDate(LocalDate date) {
            return dailystatistics.containsKey(date) ? dailystatistics.get(date).deaths : 0;
        }
        public int getConfirmedCases(LocalDate date){
            return getCasesAtDate(date);
        }
        public int getDeaths(LocalDate date) {
            return getDeathsAtDate(date);
        }

    }
    //Krok 6.
    //Napisz statyczną metodę przeciążającą fromCsv, która zamiast pojedynczej nazwy kraju przyjmuje
    //tablicę takich nazw. Metoda powinna zwrócić tablicę obiektów Country. Jeżeli metoda fromCsv(String)
    //2
    //(poprzednia) rzuca wyjątek CountryNotFoundException, należy wyświetlić na standardowym wyjściu
    //wartość zwracaną przez metodę getMessage() wyjątku i pominąć to państwo w wynikowej liście.

    public static Country[] fromCsv(String[] countries) {
            List<Country> countriesList = new ArrayList<>();
            for(String name : countries){
                try{
                    Country country = fromCsv(name);
                    if(country != null){
                        countriesList.add(country);
                    }
                }  catch (CountryNotFoundException | FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            return countriesList.toArray(new Country[0]);
    }
    //Krok 7.
    //W klasie Country napisz publiczne, czysto wirtualne metody getConfirmedCases oraz getDeaths,
    //które przyjmują jako parametr datę, a zwracającą odpowiednio liczbę zdiagnozowanych przypadków i
    //liczbę zgonów tego dnia. Zakładamy poprawność podanej daty.
    //Metody te powinny być zaimplementowane w klasach dziedziczących po Country:
    //• w CountryWithoutProvinces należy podać wartości zapisane w zdefiniowanej strukturze,
    //• w CountryWithProvinces należy wywołać tę metodę rekurencyjnie dla wszystkich prowincji i zsumować wynik

    static class CountryWithProvinces extends Country{
        public final Country[] provinces;

        public CountryWithProvinces(String name, Country[] provinces) {
            super(name);
            this.provinces = provinces;
        }

        @Override
        public int getConfirmedCases(LocalDate date) {
            int sum = 0;
            for(Country province : provinces){
                sum+= province.getConfirmedCases(date);
            }
            return sum;
        }
        public int getDeaths(LocalDate date) {
            int sum = 0;
            for(Country province: provinces){
                sum += province.getDeaths(date);
            }
            return sum;
        }
    }
    public static void sortByDeaths(List<Country> countries, LocalDate startDate, LocalDate endDate) {
        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                int deaths1 = getTotalDeathsinRange(o1, startDate, endDate);
                int deaths2 = getTotalDeathsinRange(o2, startDate, endDate);
                return Integer.compare(deaths1, deaths2);
            }
        });
    }
    private static int getTotalDeathsinRange(Country country, LocalDate start, LocalDate end){
        int total = 0;
        for(LocalDate date = start; !date.isAfter(end); date  = date.plusDays(1)){
            total += country.getDeaths(date);
        }
        return total;
    }
    //Krok 9.
    //W klasie Country napisz publiczną metodę saveToDataFile, która przyjmie ścieżkę do pliku wynikowego. Zakładamy, że jest ona poprawna. Metoda powinna utworzyć plik składający się z trzech kolumn
    //oddzielonych tabulatorami. W pierwszej kolumnie powinny znaleźć się daty w formacie d.MM.yy w drugiej liczba zdiagnozowanych przypadków w tym dniu, a w trzeciej liczba zgonów w tym dniu. W kolejnych
    //wierszach pliku wynikowego należy zapisać wszystkie daty i odpowiadające im statystyki dostępne w plikach CSV.
    public void saveToDataFile(String outputPath){
        DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("d.MM.yy");
        List<LocalDate> dates = getAllAvailableDates();
        Collections.sort(dates);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))){
            for(LocalDate date : dates){
                int cases = getConfirmedCases(date);
                int deaths = getDeaths(date);
                String line = String.format("%s\t%d\t%d", date.format(outFormatter), cases, deaths);
                writer.write((line));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<LocalDate> getAllAvailableDates(){
        return new ArrayList<>();
    }
}
