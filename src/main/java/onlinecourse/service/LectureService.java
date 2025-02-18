package onlinecourse.service;

        import onlinecourse.dto.LectureDTO;
        import onlinecourse.dto.StudentDTO;
        import onlinecourse.model.Lecture;
        import onlinecourse.repository.LectureRepository;
        import org.springframework.beans.factory.annotation.Autowired;
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
                    // 기본 정렬 기준을 설정하거나 다른 정렬 기준을 추가할 수 있습니다.
                    lectures = lectureRepository.findAll();
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
                lecture.setCreatedAt(LocalDateTime.now());
                lecture.setUpdatedAt(LocalDateTime.now());
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
                lectureRepository.deleteById(id);
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
                            studentDTO.setEnrolledAt(student.getEnrollmentDate());
                            return studentDTO;
                        })
                        .collect(Collectors.toList()));
                dto.setCategory(lecture.getCategory().name());
                dto.setCreatedAt(lecture.getCreatedAt());
                dto.setUpdatedAt(lecture.getUpdatedAt());
                return dto;
            }
        }