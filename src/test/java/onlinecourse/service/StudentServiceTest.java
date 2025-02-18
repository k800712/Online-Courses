package onlinecourse.service;

import onlinecourse.model.Student;
import onlinecourse.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    public StudentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStudent() {
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setNickname("test");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);

        assertNotNull(createdStudent);
        assertEquals("test@example.com", createdStudent.getEmail());
        assertEquals("test", createdStudent.getNickname());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void deleteStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setDeleted(false);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        assertTrue(student.isDeleted());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void deleteStudent_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.deleteStudent(1L);
        });

        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
    }
}