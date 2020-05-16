import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        
        Path path = getFilePath();
        List<String> words = readFile(path);

        words = getSortedWords(words);
        System.out.println("Все слова в алфавитном порядке:");
        words.forEach(System.out::println);

        Map<String, Integer> stat = getStatMap(words);
        AtomicInteger sum = new AtomicInteger();

        stat.forEach((word, count) -> sum.addAndGet(count));

        System.out.println("\nОбщее кол-во слов: " + sum + "\nСтатистика слов:");
        stat.keySet().stream()
                .sorted(String::compareToIgnoreCase)
                .forEach(
                        word -> printWordStat(
                                word,
                                stat.get(word)
                        )
                );

        List<String> top = getTopWords(stat);
        System.out.println("\nМаксимально часто встречающиеся слова:");
        top.forEach(
                word -> printWordStat(
                        word,
                        stat.get(word)
                )
        );
    }

    private static Path getFilePath() {
        System.out.print("Укажите путь к файлу: ");
        String path = keyboard.nextLine();

        return Paths.get(path).toAbsolutePath();
    }

    private static List<String> readFile(Path path) {
        List<String> words = new ArrayList<>();

        try {
            try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8)) {
                scanner.useDelimiter("[ .,?!:;()\"'/\\-\\r\\n]+");

                while (scanner.hasNext()) {
                    words.add(
                            scanner.next()
                    );
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Не удалось прочитать файл", exception);
        }

        return words;
    }

    private static List<String> getSortedWords(List<String> words) {
        return words.stream()
                .sorted(String::compareToIgnoreCase)
                .collect(
                        Collectors.toList()
                );
    }

    private static Map<String, Integer> getStatMap(List<String> words) {
        Map<String, Integer> stat = new HashMap<>();

        words.forEach(
                word -> {
                    word = word.toLowerCase();

                    if (!stat.containsKey(word)) {
                        stat.put(word, 0);
                    }

                    stat.put(
                            word,
                            stat.get(word) + 1
                    );
                }
        );

        return stat;
    }

    private static List<String> getTopWords(Map<String, Integer> stat) {
        List<String> words = stat.keySet().stream()
                .sorted(
                        (a, b) -> stat.get(b).compareTo(
                                stat.get(a)
                        )
                )
                .collect(
                        Collectors.toList()
                );

        int max = stat.get(
                words.get(0)
        );

        return words.stream()
                .filter(
                        word -> stat.get(word).equals(max)
                )
                .collect(
                        Collectors.toList()
                );
    }

    private static void printWordStat(String word, int count) {
        System.out.println(
                String.format(
                        "%s - %d",
                        word,
                        count
                )
        );
    }
}

