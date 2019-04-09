package Java.ru.geekbrains.lesson3;

import java.util.*;

public class PhoneBook {
    //массив для хранения телефонного справочника
    //для каждого абонента создаётся свой массив с номерами телефонов
    private static Map<String, List<String>> caller = new HashMap<>();

    public static void secondTask() {
        String name = "Сидоров";
        String name1 = "Иванов";
        String name2 = "Васечкин";

        //заполняю телефонный справочник
        addCaller("Иванов", "599-67-89 #46");
        addCaller("Петров", "000-11-22-333");
        addCaller("Сидоров", "999-55-22-4444");
        addCaller("Сидоров", "666-22-44-555");
        System.out.println(String.format("Абонент %s - телефон %s", name, getCaller(name)));
        System.out.println(String.format("Абонент %s - телефон %s", name1, getCaller(name1)));
        System.out.println(String.format("Абонент %s - телефон %s", name2, getCaller(name2)));
    }

    //метод для заполнения телефонного справочника
    private static void addCaller(String name, String phoneNumber) {

        if (!caller.containsKey(name)) {
            caller.put(name, new ArrayList<String>());//Не найден абонент. Добавляю абонента и новый массив для хранения номеров
        }
        caller.get(name).add(phoneNumber);//Добавляю номер телефона
    }

    //метод для получения номера телефона по имени абонента
    private static String getCaller(String name) {
        String unKnownCaller = "не определён";
        if (caller.get(name) != null) {
            return caller.get(name).toString();
        } else {
            return unKnownCaller;
        }
    }
}
