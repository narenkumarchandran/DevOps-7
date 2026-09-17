package com.devops.student.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Student entity representing a student record in the system.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Roll number is required")
    @Column(nullable = false, unique = true)
    private String rollNumber;

    @NotNull(message = "Grade is required")
    @Column(nullable = false)
    private Double grade;

    @Column
    private String department;

    // Default constructor (required by JPA)
    public Student() {}

    public Student(String name, String rollNumber, Double grade, String department) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.department = department;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', rollNumber='" + rollNumber +
               "', grade=" + grade + ", department='" + department + "'}";
    }
}
