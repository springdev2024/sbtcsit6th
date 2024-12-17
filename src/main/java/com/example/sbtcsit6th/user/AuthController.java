package com.example.sbtcsit6th.user;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.sbtcsit6th.AuthService;
import com.example.sbtcsit6th.ValidationError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

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

		user.setType(UserType.CUSTOMER);
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
	public String processLogin(LoginForm loginForm, Model model, HttpServletResponse httpResponse) {

		Optional<User> optionalUser = userRepository.findByUsernameAndPassword(loginForm.getUsername(),
				loginForm.getPassword());

		if (optionalUser.isPresent()) {
			
			User loggedInUser = optionalUser.get();
			
			authService.setSession(httpResponse, loggedInUser);
						
			if(loggedInUser.getType() == UserType.CUSTOMER) {
				return "redirect:/customer";
			} else if(loggedInUser.getType() == UserType.ADMIN) {
				return "redirect:/admin";
			} else {
				return "redirect:/";
			}
			
		} else {

			model.addAttribute("error", new ValidationError("It appears you forgot your credentials."));

			return "login.html";
		}

	}
	
	@PostMapping("/logout")
	public String processLogout(HttpServletRequest httpRequest) {
		
		authService.revokeSession(httpRequest);

		return "redirect:/";

	}

}
