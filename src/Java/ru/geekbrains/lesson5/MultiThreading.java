package Java.ru.geekbrains.lesson5;

import static Java.ru.geekbrains.lesson5.OneThread.*;


public class MultiThreading {
    private static final int h=arrOneThread.length/2;//чтобы константы оставались приватными
    private static float[] arrMultiThread1 = new float[h];
    private static float[] arrMultiThread2 = new float[h];
    private static final long a = System.currentTimeMillis();
    private static Thread thread1, thread2;


    public static long speedMultiThreading() throws InterruptedException {
        long id1, id2 = 0;
        long a = System.currentTimeMillis();
        System.arraycopy(arrOneThread, 0, arrMultiThread1, 0, h);
        System.arraycopy(arrOneThread, h, arrMultiThread2, 0, h);

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                calcArray(arrMultiThread1, 0);
            }
        });
        thread1.start();

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                calcArray(arrMultiThread2, h);
            }
        });
        thread2.start();
        thread1.join();
        thread2.join();

        System.arraycopy(arrMultiThread1, 0, arrOneThread, 0, h);
        System.arraycopy(arrMultiThread2, 0, arrOneThread, h, h);
        return System.currentTimeMillis() - a;
    }

    private static void calcArray(float[] arr, int shift) {
        for (int n = 0;
             n < h - 1; n++) {
            arr[n] = (float) (arr[n] * Math.sin(0.2f + (n + shift) / 5) * Math.cos(0.2f + (n + shift) / 5) * Math.cos(0.4f + (n + shift) / 2));
        }

    }
}
