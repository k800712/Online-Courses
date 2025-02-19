// src/main/java/onlinecourse/model/Student.java
    package onlinecourse.model;

    import jakarta.persistence.*;
    import java.time.LocalDateTime;

    @Entity
    public class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String email;
        private String nickname;
        private String password;
        private LocalDateTime enrolledAt;
        private boolean isDeleted = false;

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
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }
    }