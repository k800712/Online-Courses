package onlinecourse.controller;

    import onlinecourse.dto.LectureDTO;
    import onlinecourse.model.Lecture;
    import onlinecourse.service.LectureService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/lectures")
    public class LectureController {
        @Autowired
        private LectureService lectureService;

        @GetMapping
        public List<LectureDTO> getAllLectures(@RequestParam(defaultValue = "createdAt") String sortBy) {
            return lectureService.getAllLectures(sortBy);
        }

        @GetMapping("/{id}")
        public LectureDTO getLectureById(@PathVariable Long id) {
            return lectureService.getLectureById(id);
        }

        @PostMapping
        public Lecture createLecture(@RequestBody Lecture lecture) {
            return lectureService.createLecture(lecture);
        }

        @PutMapping("/{id}")
        public Lecture updateLecture(@PathVariable Long id, @RequestBody Lecture lectureDetails) {
            return lectureService.updateLecture(id, lectureDetails);
        }

        @DeleteMapping("/{id}")
        public void deleteLecture(@PathVariable Long id) {
            lectureService.deleteLecture(id);
        }
    }