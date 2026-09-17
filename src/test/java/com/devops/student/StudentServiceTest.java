package com.devops.student;

import com.devops.student.model.Student;
import com.devops.student.repository.StudentRepository;
import com.devops.student.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudentService.
 * Tests the two core functional operations: adding and viewing student records.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student("Alice Johnson", "CS2021001", 9.2, "Computer Science");
        student1.setId(1L);

        student2 = new Student("Bob Smith", "CS2021002", 8.5, "Computer Science");
        student2.setId(2L);
    }

    // -------------------------------------------------------------------------
    // Tests for addStudent
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Add student - success when roll number is unique")
    void testAddStudent_Success() {
        // Arrange
        when(studentRepository.existsByRollNumber("CS2021001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // Act
        Student result = studentService.addStudent(student1);

        // Assert
        assertNotNull(result, "Saved student should not be null");
        assertEquals(1L, result.getId(), "ID should be 1");
        assertEquals("Alice Johnson", result.getName(), "Name should match");
        assertEquals("CS2021001", result.getRollNumber(), "Roll number should match");
        assertEquals(9.2, result.getGrade(), "Grade should match");

        verify(studentRepository, times(1)).existsByRollNumber("CS2021001");
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    @DisplayName("Add student - throws exception when roll number already exists")
    void testAddStudent_DuplicateRollNumber_ThrowsException() {
        // Arrange: roll number already exists
        when(studentRepository.existsByRollNumber("CS2021001")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.addStudent(student1),
            "Should throw IllegalArgumentException for duplicate roll number"
        );

        assertTrue(exception.getMessage().contains("CS2021001"),
                "Exception message should mention the roll number");

        // Save should never be called
        verify(studentRepository, never()).save(any(Student.class));
    }

    // -------------------------------------------------------------------------
    // Tests for getAllStudents
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Get all students - returns list of students")
    void testGetAllStudents_ReturnsList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertNotNull(students, "Student list should not be null");
        assertEquals(2, students.size(), "Should return 2 students");
        assertEquals("Alice Johnson", students.get(0).getName());
        assertEquals("Bob Smith", students.get(1).getName());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all students - returns empty list when no students exist")
    void testGetAllStudents_EmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(List.of());

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertNotNull(students, "Should return an empty list, not null");
        assertTrue(students.isEmpty(), "List should be empty");
    }

    // -------------------------------------------------------------------------
    // Tests for getStudentById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Get student by ID - returns student when found")
    void testGetStudentById_Found() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertTrue(result.isPresent(), "Student should be found");
        assertEquals("Alice Johnson", result.get().getName());
    }

    @Test
    @DisplayName("Get student by ID - returns empty when not found")
    void testGetStudentById_NotFound() {
        // Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(99L);

        // Assert
        assertFalse(result.isPresent(), "Student should not be found");
    }

    // -------------------------------------------------------------------------
    // Tests for deleteStudent
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Delete student - success when student exists")
    void testDeleteStudent_Success() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> studentService.deleteStudent(1L));

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete student - throws exception when student not found")
    void testDeleteStudent_NotFound_ThrowsException() {
        // Arrange
        when(studentRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.deleteStudent(99L)
        );

        assertTrue(exception.getMessage().contains("99"),
                "Exception message should mention the ID");

        verify(studentRepository, never()).deleteById(any());
    }
}
