package onlinecourse.controller;

import onlinecourse.dto.EnrollmentDTO;
import onlinecourse.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public EnrollmentDTO enroll(@RequestParam Long studentId, @RequestParam Long lectureId) {
        return enrollmentService.enroll(studentId, lectureId);
    }
}