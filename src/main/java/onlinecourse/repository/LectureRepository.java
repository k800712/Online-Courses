// src/main/java/onlinecourse/repository/LectureRepository.java
package onlinecourse.repository;

import onlinecourse.model.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByTitleContainingAndCategory(String title, String category);
    List<Lecture> findByInstructorNameContainingAndCategory(String instructorName, String category);
    Page<Lecture> findByOrderByStudentCountDesc(Pageable pageable);
    List<Lecture> findByStudentsId(Long studentId);
    List<Lecture> findAllByOrderByCreatedAtDesc();
    List<Lecture> findByIsPrivateFalse();
    Optional<Object> findByTitle(String title);
}