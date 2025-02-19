// src/test/java/onlinecourse/service/StudentServiceTest.java
package onlinecourse.service;

import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStudent_Success() {
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setNickname("test");
        student.setPassword("password");

        when(studentRepository.findByEmailAndDeletedFalse(student.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(student.getPassword())).thenReturn("hashedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);

        assertNotNull(createdStudent);
        assertEquals("test@example.com", createdStudent.getEmail());
        assertEquals("test", createdStudent.getNickname());
        assertEquals("hashedPassword", createdStudent.getPassword());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void createStudent_EmailAlreadyExists() {
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setNickname("test");
        student.setPassword("password");

        when(studentRepository.findByEmailAndDeletedFalse(student.getEmail())).thenReturn(Optional.of(student));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.createStudent(student);
        });

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void deleteStudent_Success() {
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

    @Test
    void registerLectures_Success() {
        List<Lecture> lectures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lectures.add(new Lecture());
        }

        studentService.registerLectures(lectures);

        verify(lectureRepository, times(1)).saveAll(lectures);
    }

    @Test
    void registerLectures_TooManyLectures() {
        List<Lecture> lectures = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            lectures.add(new Lecture());
        }

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.registerLectures(lectures);
        });

        assertEquals("한 번에 최대 10개의 강의만 등록할 수 있습니다.", exception.getMessage());
        verify(lectureRepository, never()).saveAll(lectures);
    }
    @Test
    void deleteLoggedInStudent_Success() {
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setDeleted(false);

        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(studentRepository.findByEmailAndDeletedFalse("test@example.com")).thenReturn(Optional.of(student));

        studentService.deleteLoggedInStudent();

        assertTrue(student.isDeleted());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void deleteLoggedInStudent_NotFound() {
        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(studentRepository.findByEmailAndDeletedFalse("test@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.deleteLoggedInStudent();
        });

        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
    }
}
