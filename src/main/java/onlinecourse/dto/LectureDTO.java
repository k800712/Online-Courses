package onlinecourse.dto;

        import java.time.LocalDateTime;
        import java.util.List;

        public class LectureDTO {
            private Long id;
            private String title;
            private String description;
            private double price;
            private int studentCount;
            private List<StudentDTO> students;
            private String category;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getStudentCount() {
                return studentCount;
            }

            public void setStudentCount(int studentCount) {
                this.studentCount = studentCount;
            }

            public List<StudentDTO> getStudents() {
                return students;
            }

            public void setStudents(List<StudentDTO> students) {
                this.students = students;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public LocalDateTime getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
            }

            public LocalDateTime getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
            }
        }