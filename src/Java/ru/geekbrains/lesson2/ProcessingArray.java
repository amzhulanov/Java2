package Java.ru.geekbrains.lesson2;

public class ProcessingArray {

    //метод для проверки наличия символа в массиве и подсчёте суммы
    //при нахождении символа срабатывает исключение MyArrayDataException
    //при размерности не равной 4 срабатывает исключение MyArraySizeException
    public static void checkCharacterSumArray(String myArray[][]) throws MyArraySizeException, MyArrayDataException  {
        int positionColumn = 0;
        int positionRow = 0;
        int sum=0;

        if (myArray.length!=4){
            throw new MyArraySizeException(String.format("Размерность массива ограничена 4х4. Длина вашего массива %s.", myArray.length));
        }
        try{
            for ( positionRow = 0; positionRow <4 ; positionRow++) {
                for (  positionColumn= 0; positionColumn <4 ; positionColumn++) {
                    sum=sum+Integer.valueOf(myArray[positionRow][positionColumn]);
                }
            }
        }catch (NumberFormatException ex) {
            throw new MyArrayDataException("Найден символ, отличный от числа.",ex,positionRow+1,positionColumn+1);
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
