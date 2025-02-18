package onlinecourse.service;

    import onlinecourse.dto.LectureDTO;
    import onlinecourse.model.Category;
    import onlinecourse.model.Lecture;
    import onlinecourse.repository.LectureRepository;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;

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

            when(lectureRepository.findAll()).thenReturn(Collections.singletonList(lecture));

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
    }