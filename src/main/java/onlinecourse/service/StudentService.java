package onlinecourse.service;

import onlinecourse.model.Student;
import onlinecourse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        student.setEnrolledAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Student existingStudent = student.get();
            existingStudent.setDeleted(true);
            studentRepository.save(existingStudent);
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }
}