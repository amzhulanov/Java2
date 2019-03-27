package Java.ru.geekbrains.lesson1.animal;

import Java.ru.geekbrains.lesson1.Participant;
import Java.ru.geekbrains.lesson1.enums.Color;

public class Dog extends Animal implements Participant {

    public Dog(String name, Color color, int age) {
        super(name, color, age);
    }

    public Dog(String name, Color color) {
        super(name, color, 0);
    }

    @Override
    public void voice() {
        System.out.println("Гав");
    }

    @Override
    public boolean isOnDistance() {
        return false; // TODO доработать по аналогии с классом Cat
    }

    @Override
    public void run(int distance) {
        // TODO доработать по аналогии с классом Cat
    }

    @Override
    public void jump(int height) {
        // TODO доработать по аналогии с классом Cat
    }

    @Override
    public void swim(int distance) {
        // TODO доработать по аналогии с классом Cat
    }
}
