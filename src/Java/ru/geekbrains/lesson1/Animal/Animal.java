package Java.ru.geekbrains.lesson1.Animal;


import Java.ru.geekbrains.lesson1.enums.Color;

public abstract class Animal {

    private String name;
    private Color color;
    private int age;
    public String vowel="аояуеию"; //строка с глассными буквами для определения пола животного
    public boolean gender; //для определения пола животного

    public Animal(String name, Color color, int age) {
        this.name = name;
        this.color = color;
        this.age = age;
        if (vowel.indexOf((getName().charAt(getName().length()-1)))>=0) {
            gender=true;
        }else {
            gender=false;
        }
    }

    // Перегруженный (overload) конструктор
    public Animal(String name, Color color) {
        this(name, color, 0);
//        this.name = name;
//        this.color = color;
//        this.age = 0;
    }

    public abstract void voice();

    public String getName() {
        return name;
    }
    public String getParticipantName() {
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}