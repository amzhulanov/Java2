package Java.ru.geekbrains.lesson5;

import static Java.ru.geekbrains.lesson5.MultiThreading.speedMultiThreading;
import static Java.ru.geekbrains.lesson5.OneThread.setDefaultValue;
import static Java.ru.geekbrains.lesson5.OneThread.speedCalc;

public class CalcSpeed {

    public static void main(String[] args)throws InterruptedException {
        setDefaultValue();
        System.out.println(String.format("Обработка массива одним потоком длилась - %d ms", speedCalc()));
        setDefaultValue();
        System.out.println(String.format("Обработка массива двумя потоками длилась - %d ms", speedMultiThreading()));
    }

}
