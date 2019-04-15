package Java.ru.geekbrains.lesson5;

import java.util.Arrays;

public class MultiThreading {
    private static final int size = 10000000;
    private static final int h = size / 2;
    private static float[] arrOneThread = new float[size];
    private static float[] arrMultiThread1 = new float[h];
    private static float[] arrMultiThread2 = new float[h];
    private static long a = System.currentTimeMillis();
    private static Thread thread1, thread2;

    public static void speedMultiThreading() throws InterruptedException {
        //расчёт с одним потоком
        setDefaultValue();
        a = System.currentTimeMillis();
        calcArray(arrOneThread, size, 0);
        System.out.println(String.format("Обработка массива одним потоком длилась - %d ms", System.currentTimeMillis() - a));
        //расчёт с двумя потоками
        setDefaultValue();
        a = System.currentTimeMillis();
        System.arraycopy(arrOneThread, 0, arrMultiThread1, 0, h);
        System.arraycopy(arrOneThread, h, arrMultiThread2, 0, h);
        //формирую первый поток
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                calcArray(arrMultiThread1, h, 0);
            }
        });
        thread1.start();
        //формирую второй поток
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                calcArray(arrMultiThread2, h, h);
            }
        });
        thread2.start();
        thread1.join();//ожидаю завершения потоков
        thread2.join();
        //соединяю массивы
        System.arraycopy(arrMultiThread1, 0, arrOneThread, 0, h);
        System.arraycopy(arrMultiThread2, 0, arrOneThread, h, h);
        System.out.println(String.format("Обработка массива двумя потоками длилась - %d ms", System.currentTimeMillis() - a));

    }

    private static void calcArray(float[] arr, int sizeArray, int shift) {
        for (int n = 0; n < sizeArray - 1; n++) {
            arr[n] = (float) (arr[n] * Math.sin(0.2f + (n + shift) / 5) * Math.cos(0.2f + (n + shift) / 5) * Math.cos(0.4f + (n + shift) / 2));
        }
    }

    private static void setDefaultValue() {//заполняю массив одинаковыми значениями
        Arrays.fill(arrOneThread, 1);
    }

    
}
