package org.practice.task2;

public class Employee {
    private final String name;
    private final Position position;
    private final int age;

    public Employee(String name, Position position, int age) {
        this.name = name;
        this.age = age;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public int getAge() {
        return age;
    }
}
