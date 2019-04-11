package Java.ru.geekbrains.lesson1.Animal;

import Java.ru.geekbrains.lesson1.Participant;

/**
 * Робот не является животным, но может учавствовать в соревнованиях
 * так как реализует интерфейс {@link Participant}
 */
public class Robot implements Participant {

    private String name;
    private boolean isOnDistance;
    private int runDistance;
    private int jumpHeight;

    public Robot(String name) {
        this.name = name;
    }

    @Override
    public boolean isOnDistance() {
        return isOnDistance;
    }

    @Override
    public void run(int distance) {
        if (!isOnDistance) {
            return;
        }
        if (distance > runDistance) {
            isOnDistance = false;
            System.out.println(String.format("Робот %s не смог  пробежать дистанцию %d и он снимается с соревнований. На прошлых соревнованиях он достиг отметки %d", name, distance, runDistance));
        }
        return;

        //Если последний символ из имени животного гласный, то женский род.

        //System.out.println(String.format("Робот %s смог пробежать дистанцию %d", name, distance));
        // System.out.println(String.format("Робот %s смог пробежать дистанцию %d ", name, distance));
    }

    @Override
    public void jump(int height) {
        if (!isOnDistance) {
            return;
        }
        if (height > jumpHeight) {
            isOnDistance = false;
            System.out.println(String.format("Робот %s не смог взять высоту %d и он снимается с соревнований" , name, height));

            return;
        }
        System.out.println(String.format("Робот %s прыгнул на высоту %d. Её личный рекорд %d", name, height));
    }

    @Override
    public void swim(int distance) {
        isOnDistance = false;
        System.out.println("Робот был тяжёлый и утонул. Их осталось трое.");
    }

    @Override
    public String toString() {
        return "Robot{" +
                "name='" + name + '\'' +
                ", isOnDistance=" + isOnDistance +
                ", runDistance=" + runDistance +
                ", jumpHeight=" + jumpHeight +
                '}';
    }

    @Override
    public String getParticipantName() {
        return name;
    }
}