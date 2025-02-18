package onlinecourse.service;

import onlinecourse.model.Student;
import onlinecourse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        student.setEnrolledAt(LocalDateTime.now());
        return studentRepository.save(student);
    }
}