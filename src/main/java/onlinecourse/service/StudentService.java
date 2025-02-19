// src/main/java/onlinecourse/service/StudentService.java
package onlinecourse.service;

import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

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

    public Lecture createLecture(Lecture lecture) {
        if (lectureRepository.findByTitle(lecture.getTitle()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 강의 제목입니다.");
        }
        lecture.setCreatedAt(LocalDateTime.now());
        lecture.setUpdatedAt(LocalDateTime.now());
        lecture.setPrivate(true); // 기본적으로 비공개 상태로 설정
        return lectureRepository.save(lecture);
    }

    public void deleteLoggedInStudent() {
        String email = getLoggedInUserEmail();
        Optional<Student> studentOpt = studentRepository.findByEmailAndDeletedFalse(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setDeleted(true);
            studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    private String getLoggedInUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}