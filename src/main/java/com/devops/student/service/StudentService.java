package com.devops.student.service;

import com.devops.student.model.Student;
import com.devops.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Student Management business logic.
 * Handles adding and retrieving student records.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Add a new student to the system.
     *
     * @param student the student to add
     * @return the saved student with generated ID
     * @throws IllegalArgumentException if a student with the same roll number already exists
     */
    public Student addStudent(Student student) {
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new IllegalArgumentException(
                "Student with roll number '" + student.getRollNumber() + "' already exists."
            );
        }
        return studentRepository.save(student);
    }

    /**
     * Retrieve all students in the system.
     *
     * @return list of all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieve a student by their ID.
     *
     * @param id the student ID
     * @return Optional containing the student if found
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Retrieve a student by their roll number.
     *
     * @param rollNumber the roll number
     * @return Optional containing the student if found
     */
    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }

    /**
     * Delete a student by ID.
     *
     * @param id the student ID to delete
     * @throws IllegalArgumentException if student is not found
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " not found.");
        }
        studentRepository.deleteById(id);
    }
}
