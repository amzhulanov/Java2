package Java.ru.geekbrains.lesson3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UniqueWords {

    public static void firstTask(){
        System.out.println("Количество уникальных слов в массиве: " + calcWords()); //метод подсчёта уникальных слов
    }
    private static HashMap<String, Integer> calcWords() {
        //заполняю массив данными
        List<String> arrWords = Arrays.asList("Hello", "world", "I am", "new", "I am"
                , "Hello", "Yellow", "Submarin", "Beat", "Music"
                , "Hello", "look", "world", "cat", "dog"
                , "mouse", "dog", "I am");
        //создаю масив, который содержит только уникальные значения из первоначального массива
        //создаю массив для хранения пары слово-количество
        HashMap<String, Integer> calcWords = new HashMap<String, Integer>();

        //заполняю массив парой "уникальное слово"-"количество повторений"
        for (String nextItem : arrWords) {
            calcWords.merge(nextItem, 1, Integer::sum);
        }
        return calcWords;
    }

}
