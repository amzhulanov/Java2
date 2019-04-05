package Java.ru.geekbrains.lesson2;

import static Java.ru.geekbrains.lesson2.ProcessingArray.checkCharacterSumArray;
import static Java.ru.geekbrains.lesson2.ProcessingArray.fillArray;


public class CalcApplication {

    private static int dimensionArray = 4;//размерность массива
    private static String[][] myArray = new String[dimensionArray][dimensionArray];

    public static void main(String[] args) throws MyArraySizeException, MyArrayDataException{
        myArray =fillArray(myArray);
        checkCharacterSumArray(myArray);
    }
}




