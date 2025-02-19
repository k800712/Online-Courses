package onlinecourse.service;

        import onlinecourse.model.Enrollment;
        import onlinecourse.model.Student;
        import onlinecourse.model.Lecture;
        import onlinecourse.repository.EnrollmentRepository;
        import onlinecourse.repository.StudentRepository;
        import onlinecourse.repository.LectureRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;
        import java.util.stream.Collectors;

        @Service
        public class EnrollmentService {

            @Autowired
            private EnrollmentRepository enrollmentRepository;

            @Autowired
            private StudentRepository studentRepository;

            @Autowired
            private LectureRepository lectureRepository;

            public Enrollment enroll(Long studentId, Long lectureId) {
                Optional<Student> studentOpt = studentRepository.findById(studentId);
                Optional<Lecture> lectureOpt = lectureRepository.findById(lectureId);

                if (studentOpt.isEmpty()) {
                    throw new IllegalArgumentException("존재하지 않는 회원입니다.");
                }

                if (lectureOpt.isEmpty()) {
                    throw new IllegalArgumentException("존재하지 않는 강의입니다.");
                }

                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(studentOpt.get());
                enrollment.setLecture(lectureOpt.get());

                return enrollmentRepository.save(enrollment);
            }

            public List<Enrollment> enrollMultiple(Long studentId, List<Long> lectureIds) {
                return lectureIds.stream()
                        .map(lectureId -> enroll(studentId, lectureId))
                        .collect(Collectors.toList());
            }
        }