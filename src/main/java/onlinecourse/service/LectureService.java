package onlinecourse.service;

import onlinecourse.dto.LectureDTO;
import onlinecourse.dto.StudentDTO;
import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LectureService {
    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<LectureDTO> getAllLectures(String sortBy) {
        List<Lecture> lectures;
        if ("createdAt".equals(sortBy)) {
            lectures = lectureRepository.findAllByOrderByCreatedAtDesc();
        } else {
            lectures = lectureRepository.findByIsPrivateFalse(); // 비공개 강의는 목록 조회에서 제외
        }
        return lectures.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LectureDTO getLectureById(Long id) {
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture != null) {
            return convertToDTO(lecture);
        }
        return null;
    }

    public Lecture createLecture(Lecture lecture) {
        checkAdmin();
        validateLecture(lecture);
        if (lectureRepository.findByTitle(lecture.getTitle()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 강의 제목입니다.");
        }
        lecture.setCreatedAt(LocalDateTime.now());
        lecture.setUpdatedAt(LocalDateTime.now());
        lecture.setPrivate(true); // 기본적으로 비공개 상태로 설정
        return lectureRepository.save(lecture);
    }

    public Lecture updateLecture(Long id, Lecture lectureDetails) {
        checkAdmin();
        validateLecture(lectureDetails);
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture != null) {
            lecture.setTitle(lectureDetails.getTitle());
            lecture.setDescription(lectureDetails.getDescription());
            lecture.setPrice(lectureDetails.getPrice());
            lecture.setUpdatedAt(LocalDateTime.now());
            return lectureRepository.save(lecture);
        }
        return null;
    }

    public void deleteLecture(Long id) {
        checkAdmin();
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture == null) {
            throw new IllegalArgumentException("존재하지 않는 강의입니다.");
        }
        if (!lecture.getStudents().isEmpty()) {
            throw new IllegalStateException("수강 신청한 수강생이 있는 강의는 삭제할 수 없습니다.");
        }
        lectureRepository.deleteById(id);
    }

    public List<Lecture> searchByTitleAndCategory(String title, String category) {
        return lectureRepository.findByTitleContainingAndCategory(title, category);
    }

    public List<Lecture> searchByInstructorNameAndCategory(String instructorName, String category) {
        return lectureRepository.findByInstructorNameContainingAndCategory(instructorName, category);
    }

    public Page<Lecture> getLecturesSortedByStudentCount(int page, int size) {
        return lectureRepository.findByOrderByStudentCountDesc(PageRequest.of(page, size));
    }

    public List<Lecture> searchByStudentId(Long studentId) {
        return lectureRepository.findByStudentsId(studentId);
    }

    public void cancelLectureRegistration(Long lectureId) {
        String email = getLoggedInUserEmail();
        Optional<Student> studentOpt = studentRepository.findByEmailAndDeletedFalse(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Optional<Lecture> lectureOpt = lectureRepository.findById(lectureId);
            if (lectureOpt.isPresent()) {
                Lecture lecture = lectureOpt.get();
                LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
                if (student.getEnrolledAt().isBefore(oneWeekAgo)) {
                    throw new IllegalStateException("수강 신청한지 일주일이 지나 취소할 수 없습니다.");
                }
                student.getLectures().remove(lecture);
                studentRepository.save(student);
            } else {
                throw new IllegalArgumentException("존재하지 않는 강의입니다.");
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    public void enrollInLecture(Long lectureId) {
        String email = getLoggedInUserEmail();
        Optional<Student> studentOpt = studentRepository.findByEmailAndDeletedFalse(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Optional<Lecture> lectureOpt = lectureRepository.findById(lectureId);
            if (lectureOpt.isPresent()) {
                Lecture lecture = lectureOpt.get();
                if (lecture.isPrivate()) {
                    throw new IllegalStateException("비공개 강의는 수강 신청할 수 없습니다.");
                }
                if (student.getLectures().contains(lecture)) {
                    throw new IllegalStateException("동일한 강의를 중복 신청할 수 없습니다.");
                }
                student.addLecture(lecture);
                studentRepository.save(student);
            } else {
                throw new IllegalArgumentException("존재하지 않는 강의입니다.");
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    public void likeLecture(Long lectureId) {
        String email = getLoggedInUserEmail();
        Optional<Student> studentOpt = studentRepository.findByEmailAndDeletedFalse(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Optional<Lecture> lectureOpt = lectureRepository.findById(lectureId);
            if (lectureOpt.isPresent()) {
                Lecture lecture = lectureOpt.get();
                student.likeLecture(lecture);
                studentRepository.save(student);
            } else {
                throw new IllegalArgumentException("존재하지 않는 강의입니다.");
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    public List<LectureDTO> getLikedLectures() {
        String email = getLoggedInUserEmail();
        Optional<Student> studentOpt = studentRepository.findByEmailAndDeletedFalse(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            return student.getLikedLectures().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    public Lecture makeLecturePublic(Long id) {
        checkAdmin();
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
        lecture.setPrivate(false);
        return lectureRepository.save(lecture);
    }

    public Lecture makeLecturePrivate(Long id) {
        checkAdmin();
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
        if (!lecture.getStudents().isEmpty()) {
            throw new IllegalStateException("수강 신청한 수강생이 있는 강의는 비공개로 전환할 수 없습니다.");
        }
        lecture.setPrivate(true);
        return lectureRepository.save(lecture);
    }

    private void checkAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            if (!user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new IllegalStateException("관리자만 이 작업을 수행할 수 있습니다.");
            }
        } else {
            throw new IllegalStateException("관리자만 이 작업을 수행할 수 있습니다.");
        }
    }

    private void validateLecture(Lecture lecture) {
        if (lecture.getTitle() == null || lecture.getTitle().length() < 2 || lecture.getTitle().length() > 50) {
            throw new IllegalArgumentException("강의 제목은 2자 이상, 50자 이하여야 합니다.");
        }
        if (lecture.getInstructor() == null || lecture.getInstructor().getId() == null) {
            throw new IllegalArgumentException("강사 ID는 필수입니다.");
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

    private LectureDTO convertToDTO(Lecture lecture) {
        LectureDTO dto = new LectureDTO();
        dto.setId(lecture.getId());
        dto.setTitle(lecture.getTitle());
        dto.setDescription(lecture.getDescription());
        dto.setPrice(lecture.getPrice());
        dto.setStudentCount(lecture.getStudents().size());
        dto.setStudents(lecture.getStudents().stream()
                .map(student -> {
                    StudentDTO studentDTO = new StudentDTO();
                    studentDTO.setNickname(student.getNickname());
                    studentDTO.setEnrolledAt(student.getEnrolledAt());
                    return studentDTO;
                })
                .collect(Collectors.toList()));
        dto.setCategory(lecture.getCategory().name());
        dto.setCreatedAt(lecture.getCreatedAt());
        dto.setUpdatedAt(lecture.getUpdatedAt());
        return dto;
    }
}