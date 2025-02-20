// src/main/java/onlinecourse/controller/StudentController.java
package onlinecourse.controller;

import onlinecourse.dto.StudentDTO;
import onlinecourse.dto.LectureDTO;
import onlinecourse.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO createdStudent = studentService.createStudent(studentDTO);
            return ResponseEntity.ok(createdStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PostMapping("/lectures")
    public ResponseEntity<String> registerLectures(@RequestBody List<LectureDTO> lectureDTOs) {
        try {
            studentService.registerLectures(lectureDTOs);
            return ResponseEntity.ok("강의가 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}