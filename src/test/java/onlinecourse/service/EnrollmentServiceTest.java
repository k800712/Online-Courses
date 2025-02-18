package onlinecourse.service;

    import onlinecourse.model.Enrollment;
    import onlinecourse.model.Student;
    import onlinecourse.model.Lecture;
    import onlinecourse.repository.EnrollmentRepository;
    import onlinecourse.repository.StudentRepository;
    import onlinecourse.repository.LectureRepository;
    import org.junit.jupiter.api.Test;
    import org.mockito.ArgumentCaptor;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;

    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    class EnrollmentServiceTest {

        @Mock
        private EnrollmentRepository enrollmentRepository;

        @Mock
        private StudentRepository studentRepository;

        @Mock
        private LectureRepository lectureRepository;

        @InjectMocks
        private EnrollmentService enrollmentService;

        public EnrollmentServiceTest() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void enroll() {
            Student student = new Student();
            student.setId(1L);

            Lecture lecture = new Lecture();
            lecture.setId(1L);

            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setLecture(lecture);

            when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

            Enrollment createdEnrollment = enrollmentService.enroll(1L, 1L);

            assertNotNull(createdEnrollment);
            assertEquals(student, createdEnrollment.getStudent());
            assertEquals(lecture, createdEnrollment.getLecture());

            ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
            verify(enrollmentRepository, times(1)).save(enrollmentCaptor.capture());
            Enrollment savedEnrollment = enrollmentCaptor.getValue();

            assertEquals(student, savedEnrollment.getStudent());
            assertEquals(lecture, savedEnrollment.getLecture());
        }

        @Test
        void enroll_StudentNotFound() {
            when(studentRepository.findById(1L)).thenReturn(Optional.empty());

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                enrollmentService.enroll(1L, 1L);
            });

            assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
        }

        @Test
        void enroll_LectureNotFound() {
            Student student = new Student();
            student.setId(1L);

            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(lectureRepository.findById(1L)).thenReturn(Optional.empty());

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                enrollmentService.enroll(1L, 1L);
            });

            assertEquals("존재하지 않는 강의입니다.", exception.getMessage());
        }
    }