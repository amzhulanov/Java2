package Java.ru.geekbrains.lesson2;

import java.util.InputMismatchException;
import java.util.Scanner;

import static Java.ru.geekbrains.lesson2.EditArray.checkNumber;
import static Java.ru.geekbrains.lesson2.EditArray.readArray;
import static Java.ru.geekbrains.lesson2.EditArray.writeArray;

public class CalcApplication {

    public static void main(String[] args) throws MyArraySizeException, MyArrayDataException {
        int ext=1;//переменная для выхода из цикла
        int numMassiv=0;
        try{Scanner scn=new Scanner(System.in);
        do {
            System.out.println("Выберите размерность заполнения массива от 1 до 4.");
            numMassiv = scn.nextInt();
           // if (numMassiv != 4) {
            //    throw new MyArraySizeException("Размерность массива ограничена 4х4. Вы указали " + numMassiv+". Программа завершает свою работу.");
            //} else {

                System.out.println("Заполняю массив случайными числами.");

                writeArray(numMassiv);
                readArray(numMassiv);
                System.out.println("Сумма чисел в массиве = " + checkNumber());
                System.out.println("Ещё разок ? 1-Да   0-Нет");
                ext=scn.nextInt();
            //}
        }

        while (ext==1);
        } catch  (NumberFormatException ex) {
            System.out.println("Ошибка преобразования числа " + ex+". Программа завершает свою работу.");
        }catch (InputMismatchException ex){
            System.out.println("Введено неверное число. Программа завершает свою работу.");
        }catch (IndexOutOfBoundsException ex){
            throw new MyArraySizeException("Вы нарушили правила. Размерность массива ограничена 4х4. Вы указали " + numMassiv+". Программа завершает свою работу.");
        }


    }









}




