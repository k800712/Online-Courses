// src/main/java/onlinecourse/repository/StudentRepository.java
package onlinecourse.repository;

import onlinecourse.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailAndDeletedFalse(String email);
}