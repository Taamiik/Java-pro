package org.practice.task1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestRunner {
    public static void runTests(Class<?> testClass) {
        System.out.println("Запуск тестов: " + testClass.getName());

        Object myTestClass;
        Method[] methods = testClass.getMethods();
        Constructor<?> constructor;
        try {
            constructor = testClass.getConstructor();
            myTestClass = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }


        List<Method> listTests = new ArrayList<>();
        List<Method> listBeforeSuite = new ArrayList<>();
        List<Method> listBeforeTest = new ArrayList<>();
        List<Method> listAfterSuite = new ArrayList<>();
        List<Method> listAfterTest = new ArrayList<>();
        List<Method> listCsvSource = new ArrayList<>();
        int beforeSuiteCount = 0;
        int afterSuiteCount = 0;

        // Проверяем что методов с аннотациями @BeforeSuite не больше одного
        // Проверяем что методов с аннотациями @AfterSuite не больше одного

        for (Method method : methods) {
            if (!method.getDeclaringClass().equals(testClass)) continue;

            if (method.isAnnotationPresent(BeforeSuite.class)) {
                listBeforeSuite.add(method);
                beforeSuiteCount++;
                if (beforeSuiteCount > 1) {
                    throw new RuntimeException("Методов с аннотацией BeforeSuite больше 1");
                }
                checkMethodStatic(method, "BeforeSuite");
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                listAfterSuite.add(method);
                afterSuiteCount++;
                if (afterSuiteCount > 1) {
                    throw new RuntimeException("Методов с аннотацией AfterSuite больше 1");
                }
                checkMethodStatic(method, "AfterSuite");
            }
            if (method.isAnnotationPresent(BeforeTest.class)) listBeforeTest.add(method);
            if (method.isAnnotationPresent(AfterTest.class)) listAfterTest.add(method);
            if (method.isAnnotationPresent(CsvSource.class)) listCsvSource.add(method);
            if (method.isAnnotationPresent(Test.class)) {
                if (method.getAnnotation(Test.class).priority() < 1 || method.getAnnotation(Test.class).priority() > 10){
                    throw new RuntimeException("У метода " +  method.getName() + " int должен быть в пределах от 1 до 10 включительно");
                }
                listTests.add(method);
            }
        }

        for (Method elBeforeSuite : listBeforeSuite) {
            try {
                assert elBeforeSuite != null;
                elBeforeSuite.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        if (!listTests.isEmpty()) {
            listTests.sort(Comparator.comparingInt(m -> m.getAnnotation(Test.class).priority()));

            for (Method method : listTests) {
                try {
                    for (Method elBeforeTest : listBeforeTest) {
                        elBeforeTest.invoke(null);
                    }

                    method.invoke(myTestClass);

                    for (Method elAfterTest : listAfterTest) {
                        elAfterTest.invoke(null);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Method elAfterSuite : listAfterSuite) {
            try {
                assert elAfterSuite != null;
                elAfterSuite.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        for (Method elCsvSource : listCsvSource) {
            assert elCsvSource != null;
            List<Class<?>> paramTypeList = Arrays.asList(elCsvSource.getParameterTypes());
            List<String> annotationArgList = Arrays.stream(elCsvSource.getAnnotation(CsvSource.class).value().split(",")).map(String::trim).toList();

            Object[] convertedArgs = new Object[annotationArgList.size()];

            for (int i = 0; i < annotationArgList.size(); i++) {
                if (annotationArgList.size() == paramTypeList.size()) {
                    convertedArgs[i] = typeDefinition(annotationArgList.get(i), paramTypeList.get(i));
                } else throw new ArrayIndexOutOfBoundsException("Количество аргументов метода должно быть равно 5");
            }

            try {
                elCsvSource.invoke(CsvSource.class, convertedArgs);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static Object typeDefinition(String value, Class<?> type) {

        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == String.class) {
            return value;
        }

        throw new RuntimeException("Данный тип не определен : " + type.getName());
    }

    private static void checkCountAnnotation(int count, String annotationName) {
        System.out.println("Кол-во аннотации " + annotationName + ": " + count);
        if (count > 1) {
            throw new RuntimeException("Больше одного метода с аннотацией " + annotationName);
        }
    }

    private static void checkMethodStatic(Method method, String annotationName) {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new RuntimeException("Метод с аннотацией " + annotationName + " должен быть static");
        }
    }
}