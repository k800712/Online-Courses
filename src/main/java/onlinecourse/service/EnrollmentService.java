package onlinecourse.service;

    import onlinecourse.model.Enrollment;
    import onlinecourse.model.Student;
    import onlinecourse.model.Lecture;
    import onlinecourse.repository.EnrollmentRepository;
    import onlinecourse.repository.StudentRepository;
    import onlinecourse.repository.LectureRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    @Service
    public class EnrollmentService {
        @Autowired
        private EnrollmentRepository enrollmentRepository;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private LectureRepository lectureRepository;

        public Enrollment enroll(Long studentId, Long lectureId) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setLecture(lecture);

            return enrollmentRepository.save(enrollment);
        }
    }