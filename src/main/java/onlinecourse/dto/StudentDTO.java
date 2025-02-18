package onlinecourse.dto;

import java.time.LocalDateTime;

public class StudentDTO {
    private String nickname;
    private LocalDateTime enrolledAt;


    public StudentDTO() {
    }

    public StudentDTO(String nickname, LocalDateTime enrolledAt) {
        this.nickname = nickname;
        this.enrolledAt = enrolledAt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}