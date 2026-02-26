package com.bigcompany.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final Integer managerId;
    private final List<Employee> subordinates = new ArrayList<>();

    private Employee(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.salary = builder.salary;
        this.managerId = builder.managerId;
    }

    public static class Builder {
        private int id;
        private String firstName;
        private String lastName;
        private double salary;
        private Integer managerId;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Builder managerId(Integer managerId) {
            this.managerId = managerId;
            return this;
        }

        public Employee build() {
            Objects.requireNonNull(firstName, "firstName cannot be null");
            Objects.requireNonNull(lastName, "lastName cannot be null");

            if (salary < 0) {
                throw new IllegalArgumentException("salary cannot be negative");
            }

            return new Employee(this);
        }
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getSalary() { return salary; }
    public Integer getManagerId() { return managerId; }
    public List<Employee> getSubordinates() { return subordinates; }

    public void addSubordinate(Employee e) {
        subordinates.add(e);
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName;
    }
}
