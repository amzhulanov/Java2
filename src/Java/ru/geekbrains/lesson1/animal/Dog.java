package Java.ru.geekbrains.lesson1.animal;

import Java.ru.geekbrains.lesson1.Participant;
import Java.ru.geekbrains.lesson1.enums.Color;

public class Dog extends Animal implements Participant {

    private boolean isOnDistance;
    private int runDistance;
    private int jumpHeight;

    public Dog(String name, Color color, int age, int runDistance, int jumpHeight) {
        super(name, color, age);
        this.isOnDistance=true;
        this.runDistance=runDistance;
        this.jumpHeight=jumpHeight;
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

        return isOnDistance;
    }

    @Override
    public void run(int distance) {
        if (!isOnDistance) {
            return;
        }
        if (distance > runDistance) {
            isOnDistance = false;
            if (gender){
                System.out.println(String.format("Собака %s не хватило сил пробежать %d и она снимается с соревнований. На прошлых соревнованиях она достигла отметки %d", getName(), distance,runDistance));
            }else{
                System.out.println(String.format("Псу %s не хватило сил пробежать %d и он снимается с соревнований. На прошлых соревнованиях он достиг отметки %d", getName(), distance,runDistance));
            }
            return;
        }
        //Если последний символ из имени животного гласный, то женский род.
        if (gender) {
            System.out.println(String.format("Собака %s пробежала кросс длинной %d. Её личный рекорд %d", getName(), distance,runDistance));
        }else {
            System.out.println(String.format("Пёс %s пробежал кросс длинной %d. Его личный рекорд %d", getName(), distance,runDistance));
        }
    }

    @Override
    public void jump(int height) {
        if (!isOnDistance) {
            return;
        }
        if (height > jumpHeight) {
            isOnDistance = false;
            if (gender) {
                System.out.println(String.format("Собака %s не смогла взять высоту %d и она снимается с соревнований" , getName(), height));
            }else {
                System.out.println(String.format("Пёс %s не смог взять высоту %d и он снимается с соревнований" , getName(), height));
            }
            return;
        }
        if (gender) {
            System.out.println(String.format("Собака %s прыгнула на высоту %d. Её личный рекорд %d", getName(), height, jumpHeight));
        }else {
            System.out.println(String.format("Пёс %s прыгнул на высоту %d. Его личный рекорд %d", getName(), height, jumpHeight));
        }
    }

    @Override
    public void swim(int distance) {
        if (!isOnDistance) {
            return;
        }
        if (distance > runDistance) {
            isOnDistance = false;
            if (gender) {
                System.out.println(String.format("Собака %s не кошка, но всё равно не смогла проплыть %d и снимается с соревнований.",getName(),distance));
            }else{
                System.out.println(String.format("Пёс %s не кот, но всё равно не смог проплыть %d и снимается с соревнований.",getName(),distance));
            }
            return;
        }
        if (gender) {
            System.out.println(String.format("Собака %s не кошка и смогла проплыть %d. Её личный рекорд %d.",getName(),distance,runDistance));
        }else{
            System.out.println(String.format("Пёс %s не кот и смог проплыть %d. Его личный рекорд %d.",getName(),distance,runDistance));
        }
    }

    @Override
    public String toString() {
        return "Dog{" +
                "isOnDistance=" + isOnDistance +
                ", runDistance=" + runDistance +
                ", jumpHeight=" + jumpHeight +
                '}';
    }
}