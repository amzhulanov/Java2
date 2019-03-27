package Java.ru.geekbrains.lesson1.animal;

import Java.ru.geekbrains.lesson1.Participant;

/**
 * Робот не является животным, но может учавствовать в соревнованиях
 * так как реализует интерфейс {@link Participant}
 */
public class Robot implements Participant{

    private String name;

    public Robot(String name) {
        this.name = name;
    }

    @Override
    public boolean isOnDistance() {
        return false; // TODO доработать по аналогии с классами животных
    }

    @Override
    public void run(int distance) {
        // TODO доработать по аналогии с классами животных
    }

    @Override
    public void jump(int height) {
        // TODO доработать по аналогии с классами животных
    }

    @Override
    public void swim(int distance) {
        // TODO доработать по аналогии с классами животных
    }
}
