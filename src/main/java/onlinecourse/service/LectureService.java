// src/main/java/onlinecourse/service/LectureService.java
        package onlinecourse.service;

        import onlinecourse.dto.LectureDTO;
        import onlinecourse.dto.StudentDTO;
        import onlinecourse.model.Lecture;
        import onlinecourse.repository.LectureRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.stereotype.Service;

        import java.time.LocalDateTime;
        import java.util.List;
        import java.util.stream.Collectors;

        @Service
        public class LectureService {
            @Autowired
            private LectureRepository lectureRepository;

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
                if (lectureRepository.findByTitle(lecture.getTitle()).isPresent()) {
                    throw new IllegalArgumentException("이미 존재하는 강의 제목입니다.");
                }
                lecture.setCreatedAt(LocalDateTime.now());
                lecture.setUpdatedAt(LocalDateTime.now());
                lecture.setPrivate(true); // 기본적으로 비공개 상태로 설정
                return lectureRepository.save(lecture);
            }

            public Lecture updateLecture(Long id, Lecture lectureDetails) {
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

            public Lecture makeLecturePublic(Long id) {
                Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
                lecture.setPrivate(false);
                return lectureRepository.save(lecture);
            }
        }