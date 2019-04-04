package Java.ru.geekbrains.lesson2;

public class ProcessingArray {

    //метод для проверки наличия символа в массиве и подсчёте суммы
    //при нахождении символа срабатывает исключение MyArrayDataException
    //при размерности не равной 4 срабатывает исключение MyArraySizeException
    public static void checkCharacterSumArray(String myArray[][]) throws MyArraySizeException, MyArrayDataException  {
        int positionX = 0;
        int positionY = 0;
        int sum=0;

        if (myArray.length!=4){
            throw new MyArraySizeException(String.format("Размерность массива ограничена 4х4. Длина вашего массива %s.", myArray.length));
        }
        try{
            for ( positionX = 0; positionX <4 ; positionX++) {
                for ( positionY = 0; positionY <4 ; positionY++) {
                    sum=sum+Integer.valueOf(myArray[positionX][positionY]);
                }
            }
        }catch (NumberFormatException ex) {
            throw new MyArrayDataException(String.format("В строке %s и столбце %s найден символ, отличный от числа. Программа завершает свою работу.",positionX+1,positionY+1));
        }
        System.out.println("Сумма ="+sum);
    }

    //заполняю массив случайными числами и символом
    public static String[][] fillArray(String myArray[][]){
        int dimensionArray=myArray.length;
        for (int i = 0; i < dimensionArray; i++) {
            for (int j = 0; j < dimensionArray; j++) {
                if (j == (int) (Math.random() * 10)) {
                    myArray[i][j] = "a";
                } else {
                    myArray[i][j] = String.valueOf((int) (Math.random() * 10));
                }
            }
        }
        return myArray;
    }
}
