// src/test/java/onlinecourse/service/LectureServiceTest.java
                    package onlinecourse.service;

                    import onlinecourse.dto.LectureDTO;
                    import onlinecourse.model.Category;
                    import onlinecourse.model.Lecture;
                    import onlinecourse.repository.LectureRepository;
                    import org.junit.jupiter.api.Test;
                    import org.mockito.InjectMocks;
                    import org.mockito.Mock;
                    import org.mockito.MockitoAnnotations;
                    import org.springframework.data.domain.Page;
                    import org.springframework.data.domain.PageImpl;
                    import org.springframework.data.domain.Pageable;

                    import java.util.Collections;
                    import java.util.List;
                    import java.util.Optional;
                    import java.util.ArrayList;

                    import static org.junit.jupiter.api.Assertions.*;
                    import static org.mockito.Mockito.*;

                    class LectureServiceTest {

                        @Mock
                        private LectureRepository lectureRepository;

                        @InjectMocks
                        private LectureService lectureService;

                        public LectureServiceTest() {
                            MockitoAnnotations.openMocks(this);
                        }

                        @Test
                        void getAllLectures() {
                            Lecture lecture = new Lecture();
                            lecture.setTitle("Test Lecture");
                            lecture.setStudents(new ArrayList<>()); // students 필드 초기화
                            lecture.setCategory(Category.SCIENCE); // category 필드 초기화

                            when(lectureRepository.findByIsPrivateFalse()).thenReturn(Collections.singletonList(lecture));

                            List<LectureDTO> lectures = lectureService.getAllLectures(null);

                            assertNotNull(lectures);
                            assertEquals(1, lectures.size());
                            assertEquals("Test Lecture", lectures.get(0).getTitle());
                        }

                        @Test
                        void getLectureById() {
                            Lecture lecture = new Lecture();
                            lecture.setId(1L);
                            lecture.setTitle("Test Lecture");
                            lecture.setStudents(new ArrayList<>()); // students 필드 초기화
                            lecture.setCategory(Category.SCIENCE); // category 필드 초기화

                            when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

                            LectureDTO lectureDTO = lectureService.getLectureById(1L);

                            assertNotNull(lectureDTO);
                            assertEquals("Test Lecture", lectureDTO.getTitle());
                        }

                        @Test
                        void createLecture() {
                            Lecture lecture = new Lecture();
                            lecture.setTitle("New Lecture");
                            lecture.setStudents(new ArrayList<>()); // students 필드 초기화
                            lecture.setCategory(Category.SCIENCE); // category 필드 초기화

                            when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

                            Lecture createdLecture = lectureService.createLecture(lecture);

                            assertNotNull(createdLecture);
                            assertEquals("New Lecture", createdLecture.getTitle());
                            verify(lectureRepository, times(1)).save(lecture);
                        }

                        @Test
                        void updateLecture() {
                            Lecture lecture = new Lecture();
                            lecture.setId(1L);
                            lecture.setTitle("Updated Lecture");
                            lecture.setStudents(new ArrayList<>()); // students 필드 초기화
                            lecture.setCategory(Category.SCIENCE); // category 필드 초기화

                            when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
                            when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

                            Lecture updatedLecture = lectureService.updateLecture(1L, lecture);

                            assertNotNull(updatedLecture);
                            assertEquals("Updated Lecture", updatedLecture.getTitle());
                            verify(lectureRepository, times(1)).save(lecture);
                        }

                        @Test
                        void deleteLecture() {
                            Lecture lecture = new Lecture();
                            lecture.setId(1L);
                            lecture.setStudents(new ArrayList<>()); // students 필드 초기화
                            lecture.setCategory(Category.SCIENCE); // category 필드 초기화

                            when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

                            lectureService.deleteLecture(1L);

                            verify(lectureRepository, times(1)).deleteById(1L);
                        }

                        @Test
                        void searchByTitleAndCategory() {
                            Lecture lecture = new Lecture();
                            lecture.setTitle("Test Lecture");
                            lecture.setCategory(Category.SCIENCE);

                            when(lectureRepository.findByTitleContainingAndCategory("Test", "SCIENCE"))
                                    .thenReturn(Collections.singletonList(lecture));

                            List<Lecture> lectures = lectureService.searchByTitleAndCategory("Test", "SCIENCE");

                            assertNotNull(lectures);
                            assertEquals(1, lectures.size());
                            assertEquals("Test Lecture", lectures.get(0).getTitle());
                        }

                        @Test
                        void searchByInstructorNameAndCategory() {
                            Lecture lecture = new Lecture();
                            lecture.setInstructorName("John Doe");
                            lecture.setCategory(Category.SCIENCE);

                            when(lectureRepository.findByInstructorNameContainingAndCategory("John", "SCIENCE"))
                                    .thenReturn(Collections.singletonList(lecture));

                            List<Lecture> lectures = lectureService.searchByInstructorNameAndCategory("John", "SCIENCE");

                            assertNotNull(lectures);
                            assertEquals(1, lectures.size());
                            assertEquals("John Doe", lectures.get(0).getInstructorName());
                        }

                        @Test
                        void getLecturesSortedByStudentCount() {
                            Lecture lecture = new Lecture();
                            lecture.setTitle("Popular Lecture");
                            List<Lecture> lectureList = Collections.singletonList(lecture);
                            Page<Lecture> lecturePage = new PageImpl<>(lectureList);

                            when(lectureRepository.findByOrderByStudentCountDesc(any(Pageable.class)))
                                    .thenReturn(lecturePage);

                            Page<Lecture> lectures = lectureService.getLecturesSortedByStudentCount(0, 10);

                            assertNotNull(lectures);
                            assertEquals(1, lectures.getTotalElements());
                            assertEquals("Popular Lecture", lectures.getContent().get(0).getTitle());
                        }

                        @Test
                        void searchByStudentId() {
                            Lecture lecture = new Lecture();
                            lecture.setTitle("Student's Lecture");

                            when(lectureRepository.findByStudentsId(1L))
                                    .thenReturn(Collections.singletonList(lecture));

                            List<Lecture> lectures = lectureService.searchByStudentId(1L);

                            assertNotNull(lectures);
                            assertEquals(1, lectures.size());
                            assertEquals("Student's Lecture", lectures.get(0).getTitle());
                        }

                        @Test
                        void makeLecturePublic() {
                            Lecture lecture = new Lecture();
                            lecture.setId(1L);
                            lecture.setPrivate(true);

                            when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
                            when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

                            Lecture publicLecture = lectureService.makeLecturePublic(1L);

                            assertNotNull(publicLecture);
                            assertFalse(publicLecture.isPrivate());
                            verify(lectureRepository, times(1)).save(lecture);
                        }
                    }