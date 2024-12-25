package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Thêm annotation @Repository nếu muốn
public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email); // Sửa lỗi cú pháp và tham số
}
