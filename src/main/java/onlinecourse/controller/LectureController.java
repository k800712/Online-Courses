package onlinecourse.controller;

import onlinecourse.dto.LectureDTO;
import onlinecourse.service.LectureService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {


    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public List<LectureDTO> getAllLectures(@RequestParam(defaultValue = "createdAt") String sortBy) {
        return lectureService.getAllLectures(sortBy);
    }

    @GetMapping("/{id}")
    public LectureDTO getLectureById(@PathVariable Long id) {
        return lectureService.getLectureById(id);
    }

    @PostMapping
    public ResponseEntity<LectureDTO> createLecture(@RequestBody LectureDTO lectureDTO) {
        try {
            LectureDTO createdLecture = lectureService.createLecture(lectureDTO);
            return ResponseEntity.ok(createdLecture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public LectureDTO updateLecture(@PathVariable Long id, @RequestBody LectureDTO lectureDTO) {
        return lectureService.updateLecture(id, lectureDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLecture(@PathVariable Long id) {
        lectureService.deleteLecture(id);
    }

    @PutMapping("/{id}/public")
    public ResponseEntity<LectureDTO> makeLecturePublic(@PathVariable Long id) {
        try {
            LectureDTO publicLecture = lectureService.makeLecturePublic(id);
            return ResponseEntity.ok(publicLecture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<LectureDTO>> searchByTitleAndCategory(@RequestParam String title, @RequestParam String category) {
        List<LectureDTO> lectures = lectureService.searchByTitleAndCategory(title, category);
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/search/instructor")
    public ResponseEntity<List<LectureDTO>> searchByInstructorNameAndCategory(@RequestParam String instructorName, @RequestParam String category) {
        List<LectureDTO> lectures = lectureService.searchByInstructorNameAndCategory(instructorName, category);
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/sorted")
    public ResponseEntity<Page<LectureDTO>> getLecturesSortedByStudentCount(@RequestParam int page, @RequestParam int size) {
        Page<LectureDTO> lectures = lectureService.getLecturesSortedByStudentCount(page, size);
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/search/student")
    public ResponseEntity<List<LectureDTO>> searchByStudentId(@RequestParam Long studentId) {
        List<LectureDTO> lectures = lectureService.searchByStudentId(studentId);
        return ResponseEntity.ok(lectures);
    }
}