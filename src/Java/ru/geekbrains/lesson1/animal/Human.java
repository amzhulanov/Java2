package Java.ru.geekbrains.lesson1.animal;


import Java.ru.geekbrains.lesson1.Participant;
import Java.ru.geekbrains.lesson1.enums.Color;

public class Human extends Animal implements Participant {

    public Human(String name, Color color, int age) {
        super(name, color, age);
    }

    public Human(String name, Color color) {
        super(name, color, 0);
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

    @Override
    public void voice() {
        // TODO доработать по аналогии с классом Cat
    }
}
