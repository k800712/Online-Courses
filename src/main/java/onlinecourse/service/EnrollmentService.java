package onlinecourse.service;

import onlinecourse.dto.EnrollmentDTO;
import onlinecourse.model.Enrollment;
import onlinecourse.model.Student;
import onlinecourse.model.Lecture;
import onlinecourse.repository.EnrollmentRepository;
import onlinecourse.repository.StudentRepository;
import onlinecourse.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {


    private final EnrollmentRepository enrollmentRepository;

    private final StudentRepository studentRepository;

    private final LectureRepository lectureRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, LectureRepository lectureRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.lectureRepository = lectureRepository;
    }

    public EnrollmentDTO enroll(Long studentId, Long lectureId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Lecture> lectureOpt = lectureRepository.findById(lectureId);

        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        if (lectureOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 강의입니다.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(studentOpt.get());
        enrollment.setLecture(lectureOpt.get());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(savedEnrollment);
    }

    public List<EnrollmentDTO> enrollMultiple(Long studentId, List<Long> lectureIds) {
        return lectureIds.stream()
                .map(lectureId -> enroll(studentId, lectureId))
                .collect(Collectors.toList());
    }

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setStudentId(enrollment.getStudent().getId());
        enrollmentDTO.setLectureId(enrollment.getLecture().getId());
        return enrollmentDTO;
    }
}