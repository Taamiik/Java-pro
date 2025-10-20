package org.practice.task2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {

    public static void start() {
        // 1 Задача
        // Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
        List<Integer> numbers = Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13);
        var largestNumber = numbers.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst();

        System.out.println(largestNumber);

        System.out.println("'==================='");


        // 2 Задача
        // Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9, в отличие от прошлой задачи здесь разные 10 считает за одно число)
        Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13).distinct()
                .sorted(Comparator.reverseOrder()).limit(3).skip(2)
                .forEach(System.out::println);

        System.out.println("'==================='");


        // 3 Задача
        // Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
        List<Employee> listEmployees = new ArrayList<>(Arrays.asList(
                new Employee("Толя", Position.DIRECTOR, 37),
                new Employee("Михаил", Position.MANAGER, 27),
                new Employee("Антон", Position.ENGINEER, 43),
                new Employee("Андрей", Position.ENGINEER, 33),
                new Employee("Вадим", Position.MANAGER, 22)
        ));

        listEmployees.stream().filter(employee -> employee.getPosition() == Position.ENGINEER)
                .sorted(Comparator.comparing(Employee::getAge).reversed()).limit(3)
                .map(Employee::getName)
                .forEach(System.out::println);

        System.out.println("'==================='");


        // 4 Задача
        // Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст сотрудников с должностью «Инженер»
        listEmployees.stream()
                .filter(e -> e.getPosition() == Position.ENGINEER)
                .mapToDouble(Employee::getAge)
                .average()
                .ifPresent(avg -> System.out.println("Средний возраст сотрудников с должностью «Инженер»: " + avg));

        System.out.println("'==================='");


        // 5 Задача
        // Найдите в списке слов самое длинное
        List<String> strings = Arrays.asList("яблоко", "кот", "собака", "дом", "солнце", "книга", "стол");

        strings.stream()
                .sorted(Comparator.comparing(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);

        System.out.println("'==================='");


        // 6 Задача
        // Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы, в которой будут храниться пары: слово - сколько раз оно встречается во входной строке
        String input = "солнце светит ярко солнце греет сильно ветер дует сильно";

        Map<String, Long> wordCountMap = Arrays.stream(input.split(" "))
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.counting()
                ));

        System.out.println(wordCountMap);

        System.out.println("'==================='");


        // 7 Задача
        // Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок
        List<String> words = Arrays.asList("яблоко", "кот", "собака", "дом", "солнце", "книга", "стол");

        words.stream()
                .sorted(Comparator.comparing(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);

        // 8 Задача
        // Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом, найдите среди всех слов самое длинное,
        // если таких слов несколько, получите любое из них
        String[] strWords = {
                "программирование алгоритм структура функция переменная",
                "интерфейс исключение коллекция поток строка",
                "трансформация инициализация конкатенация выполнение компиляция"
        };

        Arrays.stream(strWords).
                flatMap(s -> Arrays.stream(s.split(" ")))
                .max(Comparator.comparing(String::length)).ifPresent(System.out::println);
    }
}
