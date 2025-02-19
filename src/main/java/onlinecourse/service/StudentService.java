// src/main/java/onlinecourse/service/StudentService.java
package onlinecourse.service;

import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Student createStudent(Student student) {
        Optional<Student> existingStudent = studentRepository.findByEmailAndDeletedFalse(student.getEmail());
        if (existingStudent.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
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

    public void registerLectures(List<Lecture> lectures) {
        if (lectures.size() > 10) {
            throw new IllegalArgumentException("한 번에 최대 10개의 강의만 등록할 수 있습니다.");
        }
        lectureRepository.saveAll(lectures);
    }
}