import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NonFoodProduct extends Product {
    // Используем Map для быстрого доступа к цене по ключу "годМесяц"
    private final Map<Integer, Double> pricesMap = new HashMap<>();

    // Конструктор теперь принимает название и заполняет карту цен
    NonFoodProduct(String name, Double[] pricesArray) {
        super(name);

        // Начинаем с даты 01.2010 (как указано в условии)
        int startYear = 2010;
        int startMonth = 1;

        for (Double price : pricesArray) {
            pricesMap.put(startYear * 100 + startMonth, price);

            // Переходим к следующему месяцу
            startMonth++;
            if (startMonth > 12) {
                startMonth = 1;
                startYear++;
            }
        }
    }

    public static NonFoodProduct fromCsv(Path path) {
        try (Scanner scanner = new Scanner(path)) { // Использование try-with-resources автоматически закроет сканер
            if (!scanner.hasNextLine()) return null;

            String name = scanner.nextLine(); // 1-я линия: название продукта

            if (scanner.hasNextLine()) scanner.nextLine(); // 2-я линия: пропускаем заголовок

            if (scanner.hasNextLine()) {
                Double[] pricesArray = Arrays.stream(scanner.nextLine().split(";"))
                        .map(value -> value.replace(",", "."))
                        .map(Double::valueOf)
                        .toArray(Double[]::new);

                return new NonFoodProduct(name, pricesArray);
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu pliku: " + e.getMessage());
        }
        return null;
    }

    @Override
    public double getPrice(int year, int month) throws IndexOutOfBoundsException {
        validateDate(year, month);

        // 2. Формируем ключ (например, 201001 для января 2010)
        Integer key = year * 100 + month;

        // 3. Проверяем наличие данных
        if (!pricesMap.containsKey(key)) {
            return 0.0;
        }

        return pricesMap.get(key);
    }
}