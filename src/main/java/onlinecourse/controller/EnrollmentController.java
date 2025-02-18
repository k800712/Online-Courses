package onlinecourse.controller;

import onlinecourse.model.Enrollment;
import onlinecourse.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public Enrollment enroll(@RequestParam Long studentId, @RequestParam Long lectureId) {
        return enrollmentService.enroll(studentId, lectureId);
    }
}