package Java.ru.geekbrains.lesson2;

public class MyArraySizeException extends Exception {
    public MyArraySizeException (String message){
        super(message);
    }

    public MyArraySizeException (String message,Throwable cause){
        super(message,cause);
    }
}
