package Java.ru.geekbrains.lesson2;

import java.util.Scanner;

public class EditArray {
     private static int dimensionArray=4;//размерность массива
     static String[][] myArray=new String[dimensionArray][dimensionArray];


     public static void fillArrayWithRandomNumbers() {//метод для наполнения массива случайными числами и символом "а"
            for (int i = 0; i < dimensionArray; i++) {
                for (int j = 0; j < dimensionArray; j++) {
                    if (j == (int) (Math.random() * 20)) {
                        myArray[i][j] = "a";
                    } else {
                        myArray[i][j] = String.valueOf((int) (Math.random() * 10));
                    }

                }
            }
        }

    public  static void printArray() {//метод для отображения массива

        for (int i = 0; i < dimensionArray; i++) {
            for (int j = 0; j < dimensionArray; j++) {
                System.out.print(myArray[i][j] + ":");
            }
            System.out.println();
        }
    }


}
