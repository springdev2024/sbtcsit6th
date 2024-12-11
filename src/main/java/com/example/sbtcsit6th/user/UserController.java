package com.example.sbtcsit6th.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.sbtcsit6th.ValidationError;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/register")
	public String getRegisterPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("error", new ValidationError());
		return "register.html";
	}

	@PostMapping("/register")
	public String postRegisterForm(User user, Model model) {

		/*
		 * Username should begin with alphabet Username can contain digits Username
		 * should be between 8-32 characters
		 */

		if (user.getUsername().length() < 3) {
			user.setPassword(null);

			model.addAttribute("user", user);
			model.addAttribute("error", new ValidationError("Username should contain at least 3 characters"));
			return "register.html";
		}

		// check if username already exists
		if (userRepository.existsByUsername(user.getUsername())) {

			System.out.println("Username already exists");

			// reset password field
			user.setPassword(null);

			model.addAttribute("user", user);
			model.addAttribute("error", new ValidationError("User already exist with given username"));
			return "register.html";

		}

		// check if email already exists
		if (userRepository.existsByEmail(user.getEmail())) {

			System.out.println("Email already exists");

			// reset password field
			user.setPassword(null);

			model.addAttribute("user", user);
			model.addAttribute("error", new ValidationError("User already exist with given email"));
			return "register.html";

		}

		userRepository.save(user);

		return "redirect:/";

	}

	@GetMapping("/login")
	public String getLoginPage(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		model.addAttribute("error", new ValidationError());
		return "login.html";
	}

	@PostMapping("/login")
	public String processLogin(LoginForm loginForm, Model model) {

		model.addAttribute("error", new ValidationError("Login feature coming soon!"));

		// TODO: validate that username, password exists in db
		// if so; provide session cookie
		// otherwise; show error message

		return "login.html";
	}

}
