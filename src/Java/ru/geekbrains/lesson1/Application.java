package Java.ru.geekbrains.lesson1;

import Java.ru.geekbrains.lesson1.animal.Cat;
import Java.ru.geekbrains.lesson1.animal.Dog;
import Java.ru.geekbrains.lesson1.animal.Human;
import Java.ru.geekbrains.lesson1.animal.Robot;
import Java.ru.geekbrains.lesson1.course.Course;
import Java.ru.geekbrains.lesson1.course.Cross;
import Java.ru.geekbrains.lesson1.course.Wall;
import Java.ru.geekbrains.lesson1.course.Water;
import Java.ru.geekbrains.lesson1.enums.Color;

/**
 * Класс для запуска приложения - симулятор кросса
 */
public class Application {

    public static void main(String[] args) {
        Team team = new Team(
                new Cat("Барсик", Color.BLACK, 1, 100, 5),
                new Dog("Дружок", Color.BLACK,5,150,15),
                new Human("Василий",Color.BLACK,28),
                new Robot("Android")
        );

        Course course = new Course(
                new Cross(50),
                new Wall(10),
                new Water(5)


        );
        team.showTeam();
        course.doIt(team);
        team.showResult();


    }

}