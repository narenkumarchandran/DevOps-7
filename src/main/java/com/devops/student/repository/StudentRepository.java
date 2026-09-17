package com.devops.student.repository;

import com.devops.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Student entity.
 * Spring Data JPA provides CRUD implementations automatically.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find a student by their roll number.
     *
     * @param rollNumber the roll number to search for
     * @return Optional containing the student if found
     */
    Optional<Student> findByRollNumber(String rollNumber);

    /**
     * Check if a student with the given roll number already exists.
     *
     * @param rollNumber the roll number to check
     * @return true if student exists, false otherwise
     */
    boolean existsByRollNumber(String rollNumber);
}
