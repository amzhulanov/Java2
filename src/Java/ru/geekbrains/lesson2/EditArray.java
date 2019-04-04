package Java.ru.geekbrains.lesson2;

 public class EditArray {
    static String[][] myArray=new String[4][4];


     public static void writeArray(int dimension) {//метод для наполнения массива случайными числами и символом "а"

            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (j == (int) (Math.random() * 20)) {
                        myArray[i][j] = "a";
                    } else {
                        myArray[i][j] = String.valueOf((int) (Math.random() * 10));
                    }

                }
            }
        }

    public  static void readArray(int dimension) {//метод для отображения массива

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.print(myArray[i][j] + ":");
            }
            System.out.println();
        }
    }

    public static int checkNumber() throws MyArrayDataException {//метод для проверки массива и вычисления суммы
        //int ch;
        int i=0;
        int j=0;
        int sum=0;
        try{
            for ( i = 0; i <4 ; i++) {
                for ( j = 0; j <4 ; j++) {
                    sum=sum+Integer.valueOf(myArray[i][j]);
               }
            }
        }catch (NumberFormatException ex)
                {throw new MyArrayDataException(String.format("Что-то пошло не так. В строке %s и столбце %s найден символ, отличный от числа. Программа завершает свою работу.",i+1,j+1));
        }
         return sum;
    }
}
