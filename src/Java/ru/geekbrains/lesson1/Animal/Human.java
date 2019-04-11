package Java.ru.geekbrains.lesson1.Animal;


import Java.ru.geekbrains.lesson1.Participant;
import Java.ru.geekbrains.lesson1.enums.Color;

public class Human extends Animal implements Participant {

    private boolean isOnDistance;
    private int runDistance;
    private int jumpHeight;


    public Human(String name, Color color, int age) {
        super(name, color, age);
    }

    public Human(String name, Color color) {
        super(name, color, 0);
    }

    @Override
    public boolean isOnDistance() {
        return isOnDistance;
    }

    @Override
    public void run(int distance) {
        if (!isOnDistance) {

            System.out.println("Человек не соревнуется в беге  с животными");
            return;
        }
        if (distance > runDistance) {
            isOnDistance = false;
            if (gender) {
                System.out.println(String.format("Участнице %s не хватило сил пробежать %d и она снимается с соревнований. На прошлых соревнованиях она достигла отметки %d", getName(), distance, runDistance));
            }else{
                System.out.println(String.format("Участнику %s не хватило сил пробежать %d и он снимается с соревнований. На прошлых соревнованиях он достиг отметки %d", getName(), distance, runDistance));
            }
            return;
        }
        //Если последний символ из имени животного гласный, то женский род.
        if (gender) {
            System.out.println(String.format("Участница %s пробежала кросс длинной %d. Её личный рекорд %d", getName(), distance,runDistance));
        }else {
            System.out.println(String.format("Участник %s пробежал кросс длинной %d. Его личный рекорд %d", getName(), distance,runDistance));
        }
    }

    @Override
    public void jump(int height) {
        if (!isOnDistance) {
            System.out.println("Человек не соревнуется в прыжках  с животными и снимается с соревнований");
            return;
        }
        if (height > jumpHeight) {
            isOnDistance = false;
            if (gender) {
                System.out.println(String.format("Участница %s не смогла взять высоту %d и она снимается с соревнований" , getName(), height));
            }else {
                System.out.println(String.format("Участник %s не смог взять высоту %d и он снимается с соревнований" , getName(), height));
            }
            return;
        }
        if (gender) {
            System.out.println(String.format("Участница %s прыгнула на высоту %d. Её личный рекорд %d", getName(), height, jumpHeight));
        }else {
            System.out.println(String.format("Участник %s прыгнул на высоту %d. Его личный рекорд %d", getName(), height, jumpHeight));
        }
    }

    @Override
    public void swim(int distance) {
        if (!isOnDistance) {
            System.out.println("Человек не соревнуется с животными в воде  и снимается с соревнований");
            return;
        }
        if (distance > runDistance) {
            isOnDistance = false;
            if (gender) {
                System.out.println(String.format("Участница %s не смогла проплыть %d и снимается с соревнований.",getName(),distance));
            }else{
                System.out.println(String.format("Участник %s не смог проплыть %d и снимается с соревнований.",getName(),distance));
            }
            return;
        }
        if (gender) {
            System.out.println(String.format("Участница %s проплыла %d. Её личный рекорд %d.",getName(),distance,runDistance));
        }else{
            System.out.println(String.format("Участник %s проплыл %d. Его личный рекорд %d.",getName(),distance,runDistance));
        }
    }

    @Override
    public void voice() {
        System.out.println("Здравствуйте");
    }

    @Override
    public String toString() {
        return "Human{" +
                "isOnDistance=" + isOnDistance +
                ", runDistance=" + runDistance +
                ", jumpHeight=" + jumpHeight +
                '}';
    }


}