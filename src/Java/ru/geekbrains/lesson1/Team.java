package Java.ru.geekbrains.lesson1;

import java.util.Arrays;

/**
 * Класс - комманда участников соревнований
 */
public class Team {

    private Participant[] participants;

    // здесь используется конструктор с переменным числом параметров
    public Team(Participant... participants) {
        // внутри метода переменное число параметров интерпретируется как массив
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Team{" +
                "participants=" + Arrays.toString(participants) +
                '}';


    }

    public void setParticipants(Participant[] participants) {
        this.participants = participants;
    }

    public void showResult(){
        System.out.println("Победитель в соревнованиях:");

        for (int i=0;i<participants.length;i++) {
            if (participants[i].isOnDistance()) {
                System.out.println(participants[i]);
            }
        }

    }

    public void showTeam(){
        System.out.println("Наша команда:");
        for (int i=0;i<participants.length;i++) {
            System.out.println(participants[i]);
        }





    }


    public Participant[] getParticipants() {
        return participants;
    }

}