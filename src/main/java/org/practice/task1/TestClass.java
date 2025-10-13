package org.practice.task1;

public class TestClass {
    @BeforeSuite
    public static void start() {
        System.out.println("Процесс инициализации");
    }

    @Test(priority = 4)
    public void loadingData() {
        System.out.println("Загрузить данные");
    }

    @Test(priority = 7)
    public void processData() {
        System.out.println("Обработка данных");
    }

    @Test(priority = 8)
    public void validateData() {
        System.out.println("Валидация данных");
    }

    @Test(priority = 9)
    public void exportData() {
        System.out.println("Экспорт данных");
    }

    @AfterSuite
    public static void closingConnections() {
        System.out.println("Закрытие соединений");
    }

    @AfterTest
    public static void afterLoading() {
        System.out.println("after загрузка");
    }

    @BeforeTest
    public static void reconnect() {
        System.out.println("Повторное подключение");
    }

    @CsvSource("100, Java, 20, true, 33.4")
    public static void testMethod(int a, String b, int c, boolean d, float e) {
        System.out.println("Params: " + a + ", " + b + ", " + c + ", " + d + ", " + e );
    }
}
