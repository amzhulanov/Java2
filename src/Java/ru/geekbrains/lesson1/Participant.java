package Java.ru.geekbrains.lesson1;


/**
 * Интерфейс - участник соревнований
 */
public interface Participant {
    String getParticipantName();

    boolean isOnDistance();

    void run(int distance);

    void jump(int height);

    void swim(int distance);


}
