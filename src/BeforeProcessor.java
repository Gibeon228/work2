import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeforeProcessor {

    public static void runBefore(Class<?> beforeClass) {
        final Constructor<?> declaredConstructor;
        try {
            declaredConstructor = beforeClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Для класса \"" + beforeClass.getName() + "\" не найден конструктор без аргументов");
        }

        final Object beforeObj;
        try {
            beforeObj = declaredConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект класса \"" + beforeClass.getName() + "\"");
        }

        List<Method> beforeMethods = new ArrayList<>();
        for (Method method : beforeClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                checkBeforeMethod(method);
                beforeMethods.add(method);
            }
        }
        beforeMethods.forEach(it -> runBefore(it, beforeObj));
    }

    private static void checkBeforeMethod(Method method) {
        if (!method.getReturnType().isAssignableFrom(void.class) || method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
        }
    }


    private static void runBefore(Method beforeMethod, Object beforeObj) {
        try {
            beforeMethod.invoke(beforeObj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось запустить тестовый метод \"" + beforeMethod.getName() + "\"");
        } catch (AssertionError e) {

        }
    }
}
