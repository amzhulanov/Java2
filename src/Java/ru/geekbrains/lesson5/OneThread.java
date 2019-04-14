package Java.ru.geekbrains.lesson5;


import java.util.Arrays;

import static java.lang.Math.*;

public class OneThread {
    public static final int size = 10000000;
    public static final int h = size / 2;
    public static float[] arrOneThread = new float[size];

    public static void setDefaultValue() {//заполняю массив одинаковыми значениями
        Arrays.fill(arrOneThread, 1);
    }

    public static long speedCalc() {//Обработка массива одним потоком
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arrOneThread[i] = (float) (arrOneThread[i] * sin(0.2f + i / 5) * cos(0.2f + i / 5) * cos(0.4f + i / 2));
        }

        return System.currentTimeMillis() - a;
    }

}
