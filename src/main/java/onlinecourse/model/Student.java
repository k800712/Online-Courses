package onlinecourse.model;

    import jakarta.persistence.*;
    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    public class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String email;
        private String nickname;
        private String password;
        private LocalDateTime enrolledAt;
        private boolean deleted = false;

        @ManyToMany(mappedBy = "students")
        private List<Lecture> lectures;

        @ManyToMany
        @JoinTable(
            name = "student_likes",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lecture_id")
        )
        private List<Lecture> likedLectures;

        @PrePersist
        @PreUpdate
        private void validateNickname() {
            if (nickname == null || nickname.length() < 2 || nickname.length() > 20) {
                throw new IllegalArgumentException("회원 닉네임은 2자 이상, 20자 이하여야 합니다.");
            }
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public LocalDateTime getEnrolledAt() {
            return enrolledAt;
        }

        public void setEnrolledAt(LocalDateTime enrolledAt) {
            this.enrolledAt = enrolledAt;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public List<Lecture> getLectures() {
            return lectures;
        }

        public void setLectures(List<Lecture> lectures) {
            this.lectures = lectures;
        }

        public List<Lecture> getLikedLectures() {
            return likedLectures;
        }

        public void setLikedLectures(List<Lecture> likedLectures) {
            this.likedLectures = likedLectures;
        }

        public void addLecture(Lecture lecture) {
            this.lectures.add(lecture);
        }

        public void likeLecture(Lecture lecture) {
            if (!this.likedLectures.contains(lecture)) {
                this.likedLectures.add(lecture);
            }
        }
    }