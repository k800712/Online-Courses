// src/main/java/onlinecourse/controller/LectureController.java
                    package onlinecourse.controller;

                    import onlinecourse.dto.LectureDTO;
                    import onlinecourse.model.Lecture;
                    import onlinecourse.service.LectureService;
                    import org.springframework.beans.factory.annotation.Autowired;
                    import org.springframework.data.domain.Page;
                    import org.springframework.http.ResponseEntity;
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
                        public ResponseEntity<Lecture> createLecture(@RequestBody Lecture lecture) {
                            try {
                                Lecture createdLecture = lectureService.createLecture(lecture);
                                return ResponseEntity.ok(createdLecture);
                            } catch (IllegalArgumentException e) {
                                return ResponseEntity.badRequest().body(null);
                            }
                        }

                        @PutMapping("/{id}")
                        public Lecture updateLecture(@PathVariable Long id, @RequestBody Lecture lectureDetails) {
                            return lectureService.updateLecture(id, lectureDetails);
                        }

                        @DeleteMapping("/{id}")
                        public void deleteLecture(@PathVariable Long id) {
                            lectureService.deleteLecture(id);
                        }

                        @PutMapping("/{id}/public")
                        public ResponseEntity<Lecture> makeLecturePublic(@PathVariable Long id) {
                            try {
                                Lecture publicLecture = lectureService.makeLecturePublic(id);
                                return ResponseEntity.ok(publicLecture);
                            } catch (IllegalArgumentException e) {
                                return ResponseEntity.notFound().build();
                            }
                        }

                        @GetMapping("/search/title")
                        public ResponseEntity<List<Lecture>> searchByTitleAndCategory(@RequestParam String title, @RequestParam String category) {
                            List<Lecture> lectures = lectureService.searchByTitleAndCategory(title, category);
                            return ResponseEntity.ok(lectures);
                        }

                        @GetMapping("/search/instructor")
                        public ResponseEntity<List<Lecture>> searchByInstructorNameAndCategory(@RequestParam String instructorName, @RequestParam String category) {
                            List<Lecture> lectures = lectureService.searchByInstructorNameAndCategory(instructorName, category);
                            return ResponseEntity.ok(lectures);
                        }

                        @GetMapping("/sorted")
                        public ResponseEntity<Page<Lecture>> getLecturesSortedByStudentCount(@RequestParam int page, @RequestParam int size) {
                            Page<Lecture> lectures = lectureService.getLecturesSortedByStudentCount(page, size);
                            return ResponseEntity.ok(lectures);
                        }

                        @GetMapping("/search/student")
                        public ResponseEntity<List<Lecture>> searchByStudentId(@RequestParam Long studentId) {
                            List<Lecture> lectures = lectureService.searchByStudentId(studentId);
                            return ResponseEntity.ok(lectures);
                        }
                    }