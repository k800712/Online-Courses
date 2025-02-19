package onlinecourse.service;

import onlinecourse.dto.LectureDTO;
import onlinecourse.dto.StudentDTO;
import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    public LectureService(LectureRepository lectureRepository, StudentRepository studentRepository) {
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }

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

    public LectureDTO createLecture(LectureDTO lectureDTO) {
        checkAdmin();
        Lecture lecture = convertToEntity(lectureDTO);
        validateLecture(lecture);
        if (lectureRepository.findByTitle(lecture.getTitle()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 강의 제목입니다.");
        }
        lecture.setCreatedAt(LocalDateTime.now());
        lecture.setUpdatedAt(LocalDateTime.now());
        lecture.setPrivate(true); // 기본적으로 비공개 상태로 설정
        Lecture savedLecture = lectureRepository.save(lecture);
        return convertToDTO(savedLecture);
    }

    public LectureDTO updateLecture(Long id, LectureDTO lectureDTO) {
        checkAdmin();
        Lecture lectureDetails = convertToEntity(lectureDTO);
        validateLecture(lectureDetails);
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture != null) {
            lecture.setTitle(lectureDetails.getTitle());
            lecture.setDescription(lectureDetails.getDescription());
            lecture.setPrice(lectureDetails.getPrice());
            lecture.setUpdatedAt(LocalDateTime.now());
            Lecture updatedLecture = lectureRepository.save(lecture);
            return convertToDTO(updatedLecture);
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

    public List<LectureDTO> searchByTitleAndCategory(String title, String category) {
        return lectureRepository.findByTitleContainingAndCategory(title, category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LectureDTO> searchByInstructorNameAndCategory(String instructorName, String category) {
        return lectureRepository.findByInstructorNameContainingAndCategory(instructorName, category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<LectureDTO> getLecturesSortedByStudentCount(int page, int size) {
        return lectureRepository.findByOrderByStudentCountDesc(PageRequest.of(page, size))
                .map(this::convertToDTO);
    }

    public List<LectureDTO> searchByStudentId(Long studentId) {
        return lectureRepository.findByStudentsId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

    public LectureDTO makeLecturePublic(Long id) {
        checkAdmin();
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
        lecture.setPrivate(false);
        Lecture updatedLecture = lectureRepository.save(lecture);
        return convertToDTO(updatedLecture);
    }

    public LectureDTO makeLecturePrivate(Long id) {
        checkAdmin();
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
        if (!lecture.getStudents().isEmpty()) {
            throw new IllegalStateException("수강 신청한 수강생이 있는 강의는 비공개로 전환할 수 없습니다.");
        }
        lecture.setPrivate(true);
        Lecture updatedLecture = lectureRepository.save(lecture);
        return convertToDTO(updatedLecture);
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
        dto.setTitle(lecture.getTitle());
        dto.setDescription(lecture.getDescription());
        dto.setPrice(lecture.getPrice());

        // students 필드가 null인지 확인
        if (lecture.getStudents() != null) {
            dto.setStudentCount(lecture.getStudents().size());
            dto.setStudents(lecture.getStudents().stream()
                    .map(student -> {
                        StudentDTO studentDTO = new StudentDTO();
                        studentDTO.setNickname(student.getNickname());
                        studentDTO.setEnrolledAt(student.getEnrolledAt());
                        return studentDTO;
                    })
                    .collect(Collectors.toList()));
        } else {
            dto.setStudentCount(0);
            dto.setStudents(Collections.emptyList());
        }

        // category 필드가 null인지 확인
        if (lecture.getCategory() != null) {
            dto.setCategory(lecture.getCategory().name());
        } else {
            dto.setCategory("UNKNOWN"); // 또는 적절한 기본값 설정
        }
        dto.setInstructorName(lecture.getInstructorName());
        dto.setCreatedAt(lecture.getCreatedAt());
        dto.setUpdatedAt(lecture.getUpdatedAt());
        return dto;
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