package Java.ru.geekbrains.lesson1;

import Java.ru.geekbrains.lesson1.Animal.Cat;
import Java.ru.geekbrains.lesson1.Animal.Dog;
import Java.ru.geekbrains.lesson1.Animal.Human;
import Java.ru.geekbrains.lesson1.Animal.Robot;
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
        Team team = new Team( // создаю переменную team, которая является экземпляром класса Team?
                // И эта переменная является массивом,так как содержит в себе ссылки на классы Cat,Dog,Human,Robot ?
                new Cat("Барсик", Color.BLACK, 1, 100, 5), //это ведь вызов класса Cat ?
                new Dog("Дружок", Color.BLACK,5,150,15),
                new Human("Василий",Color.BLACK,28),
                new Robot("Android")
        );

        Course course = new Course( //создаю переменную course, которая содержит ссылку или является массивом типа Course?
                new Cross(50),//и т.к. в переменную я помещаю несколько классов препятствий,
                // то эта перменная тоже является массивом классов
                new Wall(10),
                new Water(5)


        );
        team.showTeam();//вызываю метод для показа команды из переменной которая является экземпляром класса Team
        course.doIt(team);//вызываю метод действий из переменной course, которая является экземпляром класса Course
        team.showResult();//вызываю метод показа результатов из переменной которая является экземпляром класса Team


    }

}