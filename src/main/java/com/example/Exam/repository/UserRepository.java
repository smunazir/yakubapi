package com.example.Exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Exam.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer>{

}
