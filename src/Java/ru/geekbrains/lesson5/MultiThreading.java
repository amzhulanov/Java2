package Java.ru.geekbrains.lesson5;

import static Java.ru.geekbrains.lesson5.OneThread.*;

public class MultiThreading {
    private static float[] arrMultiThread1 = new float[h];
    private static float[] arrMultiThread2 = new float[h];
    long a = System.currentTimeMillis();
    private static volatile boolean waitFirstThread, waitSecondThread = false;

    public static long speedMultiThreading() throws InterruptedException {
        long delay, id1, id2 = 0;
        long a = System.currentTimeMillis();
        System.arraycopy(arrOneThread, 0, arrMultiThread1, 0, h);
        System.arraycopy(arrOneThread, h, arrMultiThread2, 0, h);

        firstThread();
        secondThread();
        while (!waitFirstThread || !waitSecondThread) {
            Thread.currentThread().sleep(10);
        }
        System.arraycopy(arrMultiThread1, 0, arrOneThread, 0, h);
        System.arraycopy(arrMultiThread2, 0, arrOneThread, h, h);
        delay = System.currentTimeMillis() - a;
        return (delay);
    }

    private static void firstThread() {

        new Thread() {
            @Override
            public void run() {
                for (int i = 0;
                     i < h - 1; i++) {
                    arrMultiThread1[i] = (float) (arrMultiThread1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                waitFirstThread = true;
            }
        }.start();
    }

    private static void secondThread() {
        new Thread() {
            @Override
            public void run() {
                for (int n = 0;
                     n < h - 1; n++) {
                    arrMultiThread2[n] = (float) (arrMultiThread2[n] * Math.sin(0.2f + (n + h) / 5) * Math.cos(0.2f + (n + h) / 5) * Math.cos(0.4f + (n + h) / 2));
                }
                waitSecondThread = true;
            }
        }.start();
    }
}
