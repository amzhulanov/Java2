package Java.ru.geekbrains.lesson2;

import java.util.InputMismatchException;
import java.util.Scanner;
import static Java.ru.geekbrains.lesson2.EditArray.*;


public class CalcApplication {
    public static int positionX=0;
    public static int positionY=0;

    public static void main(String[] args) throws MyArraySizeException, MyArrayDataException {
        int ext = 1;//переменная для выхода из цикла

        Scanner scn = new Scanner(System.in);
        try {

            do {
                System.out.println("Заполняю массив случайными числами.");
                fillArrayWithRandomNumbers();//заполняю массив случайными числами и одним символом
                if (myArray.length != 4) {
                    throw new MyArraySizeException(String.format("Размерность массива ограничена 4х4. Длина вашего массива %s.", myArray.length));
                }
                printArray();//Вывожу на экран сформированный массив

                try{
                    System.out.println("Сумма чисел в массиве = " + checkCharacterInArray());//Здесь я проверяю наличие не числа в массиве и суммирую все ячейки.

                }catch (NumberFormatException ex) {
                    throw new MyArrayDataException(String.format("В строке %s и столбце %s найден символ, отличный от числа. Программа завершает свою работу.",positionX+1,positionY+1));
                }
                System.out.println("Ещё разок ? 1-Да   0-Нет");//завершаю цикл, если ответ не равен 1
                ext = scn.nextInt();
            }
            while (ext == 1);
        }catch (InputMismatchException ex) {
            System.out.println("Введено неверное число, будьте аккуратнее. Программа завершает свою работу.");
        }
    }

    public static int checkCharacterInArray() {//метод для проверки массива и вычисления суммы
        int sum=0;
        for ( positionX = 0; positionX <4 ; positionX++) {
            for ( positionY = 0; positionY <4 ; positionY++) {
                sum=sum+Integer.valueOf(myArray[positionX][positionY]);
            }
        }
        return sum;
    }

}




