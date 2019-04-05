package Java.ru.geekbrains.lesson2;

public class MyArrayDataException extends Exception {

    public MyArrayDataException (String message){
        super(message);
    }

    public MyArrayDataException(String message,Throwable cause,int positionRow, int positionColumn){
        this(String.format(message+" Строка %s, столбец %s",positionRow,positionColumn));
    }
}
