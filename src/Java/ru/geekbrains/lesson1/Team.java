package Java.ru.geekbrains.lesson1;

import Java.ru.geekbrains.lesson1.Animal.Animal;

import java.util.Arrays;

/**
 * Класс - комманда участников соревнований
 */
public class Team {

    private Participant[] participants; //Создаю массив неопределённого типа и говорю, что переменная participants будет массивом?

    // здесь используется конструктор с переменным числом параметров --это комментарий преподавателя
    public Team(Participant... participants) {
        // внутри метода переменное число параметров интерпретируется как массив -- это комментарий преподавателя
        this.participants = participants;//что означает эта конструкция? и что присваивается?
    }

    @Override
    public String toString() {
        return "Team{" +
                "participants=" + Arrays.toString(participants) +
                '}';
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
        System.out.println();

        System.out.println("Наша команда:");
        for (int i=0;i<participants.length;i++) {
            System.out.println(participants[i].getParticipantName());
        }
    }


    public Participant[] getParticipants() {
        return participants;
    }


}