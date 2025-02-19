// src/main/java/onlinecourse/service/StudentService.java
package onlinecourse.service;

import onlinecourse.dto.LectureDTO;
import onlinecourse.dto.StudentDTO;
import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {


    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, LectureRepository lectureRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.lectureRepository = lectureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Optional<Student> existingStudent = studentRepository.findByEmailAndDeletedFalse(studentDTO.getEmail());
        if (existingStudent.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Student student = convertToEntity(studentDTO);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setEnrolledAt(LocalDateTime.now());
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
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

    public void registerLectures(List<LectureDTO> lectureDTOs) {
        if (lectureDTOs.size() > 10) {
            throw new IllegalArgumentException("한 번에 최대 10개의 강의만 등록할 수 있습니다.");
        }
        List<Lecture> lectures = lectureDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        lectureRepository.saveAll(lectures);
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

    private Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setEmail(studentDTO.getEmail());
        student.setNickname(studentDTO.getNickname());
        student.setPassword(studentDTO.getPassword());
        student.setEnrolledAt(studentDTO.getEnrolledAt());
        return student;
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail(student.getEmail());
        studentDTO.setNickname(student.getNickname());
        studentDTO.setPassword(student.getPassword());
        studentDTO.setEnrolledAt(student.getEnrolledAt());
        return studentDTO;
    }

    private Lecture convertToEntity(LectureDTO lectureDTO) {
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDTO.getTitle());
        lecture.setDescription(lectureDTO.getDescription());
        lecture.setPrice(lectureDTO.getPrice());

        // category 필드가 null인지 확인
        if (lectureDTO.getCategory() != null) {
            try {
                lecture.setCategory(Lecture.Category.valueOf(lectureDTO.getCategory()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + lectureDTO.getCategory());
            }
        } else {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }

        lecture.setCreatedAt(lectureDTO.getCreatedAt());
        lecture.setUpdatedAt(lectureDTO.getUpdatedAt());
        lecture.setInstructorName(lectureDTO.getInstructorName());
        lecture.setStudentCount(lectureDTO.getStudentCount());
        return lecture;
    }
}