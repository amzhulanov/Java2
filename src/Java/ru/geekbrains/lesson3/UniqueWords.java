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

        //заполняю массив парой "уникальное слово"-"0 повторений"
        for (String nextItem : arrWords) {
            calcWords.put(nextItem, 0);
        }
        //перебираю каждое слово в первом массиве и добавляю кол-во в "парный массив"
        int count = 0;
        for (String curWords : arrWords) {
            count = calcWords.get(curWords);//выгружаю текущее количество повторов
            calcWords.put(curWords, count++);//если повторов 0, то это слово встретилось впервые и ставлю 1, иначе увеличиваю счётчик и записываю в HashMap
        }
        return calcWords;
    }

}
