package onlinecourse.service;

import onlinecourse.dto.LectureDTO;
import onlinecourse.model.Category;
import onlinecourse.model.Lecture;
import onlinecourse.model.Student;
import onlinecourse.repository.LectureRepository;
import onlinecourse.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private StudentRepository studentRepository;

    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lectureService = new LectureService(lectureRepository, studentRepository);
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
        lecture.setTitle("Test Lecture");
        lecture.setStudents(new ArrayList<>()); // students 필드 초기화
        lecture.setCategory(Category.SCIENCE); // category 필드 초기화

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

        LectureDTO lectureDTO = lectureService.getLectureById(1L);

        assertNotNull(lectureDTO);
        assertEquals("Test Lecture", lectureDTO.getTitle());
    }

    @Test
    void deleteLecture() {
        Lecture lecture = new Lecture();
        lecture.setStudents(new ArrayList<>()); // students 필드 초기화
        lecture.setCategory(Category.SCIENCE); // category 필드 초기화

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

        // 관리자 권한 설정
        UserDetails userDetails = User.withUsername("admin").password("password").roles("ADMIN").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        lectureService.deleteLecture(1L);

        verify(lectureRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchByTitleAndCategory() {
        Lecture lecture = new Lecture();
        lecture.setTitle("Test Lecture");
        lecture.setCategory(Category.SCIENCE);
        lecture.setStudents(new ArrayList<>()); // students 필드 초기화

        when(lectureRepository.findByTitleContainingAndCategory("Test", "SCIENCE"))
                .thenReturn(Collections.singletonList(lecture));

        List<LectureDTO> lectures = lectureService.searchByTitleAndCategory("Test", "SCIENCE");

        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals("Test Lecture", lectures.get(0).getTitle());
    }

    @Test
    void searchByInstructorNameAndCategory() {
        Lecture lecture = new Lecture();
        lecture.setInstructorName("John Doe"); // instructorName 필드 초기화
        lecture.setCategory(Lecture.Category.SCIENCE);
        lecture.setStudents(new ArrayList<>()); // students 필드 초기화

        when(lectureRepository.findByInstructorNameContainingAndCategory("John", "SCIENCE"))
                .thenReturn(Collections.singletonList(lecture));

        List<LectureDTO> lectures = lectureService.searchByInstructorNameAndCategory("John", "SCIENCE");

        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals("John Doe", lectures.get(0).getInstructorName());
    }

    @Test
    void getLecturesSortedByStudentCount() {
        Lecture lecture = new Lecture();
        lecture.setTitle("Popular Lecture");
        lecture.setStudents(new ArrayList<>()); // students 필드 초기화
        lecture.setCategory(Category.SCIENCE); // category 필드 초기화
        List<Lecture> lectureList = Collections.singletonList(lecture);
        Page<Lecture> lecturePage = new PageImpl<>(lectureList);

        when(lectureRepository.findByOrderByStudentCountDesc(any(Pageable.class)))
                .thenReturn(lecturePage);

        Page<LectureDTO> lectures = lectureService.getLecturesSortedByStudentCount(0, 10);

        assertNotNull(lectures);
        assertEquals(1, lectures.getTotalElements());
        assertEquals("Popular Lecture", lectures.getContent().get(0).getTitle());
    }

    @Test
    void searchByStudentId() {
        Lecture lecture = new Lecture();
        lecture.setTitle("Student's Lecture");
        lecture.setStudents(new ArrayList<>()); // students 필드 초기화
        lecture.setCategory(Category.SCIENCE); // category 필드 초기화

        when(lectureRepository.findByStudentsId(1L))
                .thenReturn(Collections.singletonList(lecture));

        List<LectureDTO> lectures = lectureService.searchByStudentId(1L);

        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals("Student's Lecture", lectures.get(0).getTitle());
    }

    @Test
    void cancelLectureRegistration_LectureNotFound() {
        Student student = new Student();
        student.setEmail("test@example.com");

        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(studentRepository.findByEmailAndDeletedFalse("test@example.com")).thenReturn(Optional.of(student));
        when(lectureRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            lectureService.cancelLectureRegistration(1L);
        });

        assertEquals("존재하지 않는 강의입니다.", exception.getMessage());
    }
}