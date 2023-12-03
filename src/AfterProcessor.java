import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AfterProcessor {
    public static void runAfter(Class<?> afterClass) {
        final Constructor<?> declaredConstructor;
        try {
            declaredConstructor = afterClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Для класса \"" + afterClass.getName() + "\" не найден конструктор без аргументов");
        }

        final Object afterObj;
        try {
            afterObj = declaredConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект класса \"" + afterClass.getName() + "\"");
        }

        List<Method> afterMethods = new ArrayList<>();
        for (Method method : afterClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterEach.class)) {
                checkAfterMethod(method);
                afterMethods.add(method);
            }
        }
        afterMethods.forEach(it -> runAfter(it, afterObj));
    }

    private static void checkAfterMethod(Method method) {
        if (!method.getReturnType().isAssignableFrom(void.class) || method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
        }
    }


    private static void runAfter(Method afterMethod, Object afterObj) {
        try {
            afterMethod.invoke(afterObj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось запустить тестовый метод \"" + afterMethod.getName() + "\"");
        } catch (AssertionError e) {

        }
    }
}
