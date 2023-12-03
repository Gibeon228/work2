public class Homework {

    /**
     * Расширить пример с запуском тестов следующими фичами:
     * 1. Добавить аннотации BeforeEach, AfterEach,
     * которые ставятся над методами void без аругментов и запускаются ДО и ПОСЛЕ всех тестов соответственно.
     * 2. В аннотацию Test добавить параметр order() со значением 0 по умолчанию.
     * Необходимо при запуске тестов фильтровать тесты по этому параметру (от меньшего к большему).
     * Т.е. если есть методы @Test(order = -2) void first, @Test void second, Test(order = 5) void third,
     * то порядок вызовов first -> second -> third
     * 3.* Добавить аннотацию @Skip, которую можно ставить над тест-методами. Если она стоит - то тест не запускается.
     * 4.* При наличии идей, реализовать их и написать об этом в комментарии при сдаче.
     */


    public static void main(String[] args) {
        BeforeProcessor.runBefore(MyTest.class);
        TestProcessor.runTest(MyTest.class);
        AfterProcessor.runAfter(MyTest.class);
    }

    static class MyTest {

        @BeforeEach
        void beforeEach() {
            System.out.println("beforeEach запущен");
        }

        @AfterEach
        void afterEach() {
            System.out.println("afterEach запущен");
        }

        @Test(order = 1)
        void firstTest() {
            System.out.println("firstTest запущен");
        }

        @Test (order = 2)
        void secondTest() {
            System.out.println("secondTest запущен");
        }

    }




}
