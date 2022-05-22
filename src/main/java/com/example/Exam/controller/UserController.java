package com.example.Exam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Exam.model.UserModel;
import com.example.Exam.repository.UserRepository;


@RestController
@RequestMapping("/cellscop")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)

public class UserController {
	Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public UserRepository userRepo;

	@PostMapping("/user_add")
	public ResponseEntity<Map> save(@RequestBody UserModel entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Optional<UserModel> dbuser = userRepo.findById(entity.getUserId());

			if (!dbuser.isEmpty()) {
				map.put("message", "user already exists");
				map.put("status", 403);
				return ResponseEntity.status(403).body(map);

			} else {
				entity = userRepo.save(entity);
				map.put("data", entity);
				map.put("status", 200);
				map.put("message", "user successfully added");

				log.info("user added");
				return ResponseEntity.ok(map);
			}

		} catch (Exception e) {
			map.put("message", "user added failed");
			map.put("Data", null);
			map.put("error", e.getLocalizedMessage());
			log.error("userId already in use");
			return ResponseEntity.status(500).body(map);
		}

	}
	
	
	


	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserModel entity) {
		List<UserModel> user = (List<UserModel>) userRepo.findAll();
		Map<String, Object> map = new HashMap<String, Object>();

		for (UserModel other : user) {
			if (other.getUserId() == (entity.getUserId()) && other.getPassword().equals(entity.getPassword())) {
				map.put("message", "Login Successful");
				map.put("status", "success");
				map.put("data", other);
				log.info("login success");
				return ResponseEntity.ok(map);
			}
		}
		map.put("message", "Login fail!");
		map.put("status", "Failed");
		map.put("data", null);
		log.info("login failed");
		return ResponseEntity.status(409).body(map);
	}

	@PostMapping("/reset")
	public ResponseEntity<Map<String, Object>> resetpass(@RequestBody UserModel user) {

		Map<String, Object> map = new HashMap<String, Object>();
		UserModel dbu = userRepo.findById(user.getUserId()).get();
		if (dbu.getUserId() == (user.getUserId()) && dbu.getMobile().equals(user.getMobile())) {
			map.put("message", "Provided  Information Matched");
			map.put("status", "success");
			map.put("data", dbu);
			log.info("user information matched");
			return ResponseEntity.ok(map);
		}

		map.put("message", "not matched");
		map.put("status", "failed");
		map.put("data", null);
		log.error("user information not matched");
		return ResponseEntity.status(409).body(map);
	}

	@PutMapping("/update")
	public ResponseEntity<Map> update(@RequestBody UserModel user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Optional<UserModel> dbuser = userRepo.findById(user.getUserId());
			if (dbuser.isEmpty()) {
				map.put("message", "user not found");
				map.put("status", 403);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
			} else {
				user = userRepo.save(user);
				map.put("data", user);
				map.put("status", 200);
				map.put("message", "data successfully updated");
				log.info("Upadated user password");
				return ResponseEntity.ok(map);
			}

		} catch (Exception e) {
			map.put("message", "data failed to update");
			map.put("error", e.getLocalizedMessage());
			log.info("password failed to update");
			return ResponseEntity.status(500).body(map);

		}

	}

}
