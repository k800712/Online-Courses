package onlinecourse.dto;

import java.time.LocalDateTime;

public class StudentDTO {
    private String email;
    private String nickname;
    private String password;
    private LocalDateTime enrolledAt;

    public StudentDTO() {
    }

    public StudentDTO(String email, String nickname, String password, LocalDateTime enrolledAt) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.enrolledAt = enrolledAt;
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
}