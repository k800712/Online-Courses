package onlinecourse.dto;

public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long lectureId;

    // 기본 생성자
    public EnrollmentDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public EnrollmentDTO(Long id, Long studentId, Long lectureId) {
        this.id = id;
        this.studentId = studentId;
        this.lectureId = lectureId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }
}