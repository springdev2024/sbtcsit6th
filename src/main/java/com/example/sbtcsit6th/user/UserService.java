package com.example.sbtcsit6th.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void setup() {
		// TODO: write code for create ADMIN user.
		if (userRepository.count() == 0) {
			User admin = new User();
			admin.setFullname("John Doe");
			admin.setUsername("admin");
			admin.setEmail("admin@demo.com");
			admin.setPassword("admin123");
			admin.setType(UserType.ADMIN);
			userRepository.save(admin);
		}

		System.out.println("This setup() is called automatically after object construction");
	}

}
