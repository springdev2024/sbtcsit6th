package com.example.sbtcsit6th.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

class RegisterError {
	private boolean hasError = false;
	private String error;
	
	public RegisterError() {
		
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/register")
	public String getRegisterPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("error", new RegisterError());
		return "register.html";
	}

	@PostMapping("/register")
	public String postRegisterForm(User user, Model model) {

		System.out.println("fullname = " + user.getFullname());
		System.out.println("username = " + user.getUsername());
		System.out.println("password = " + user.getPassword());
		System.out.println("email = " + user.getEmail());

		if (userRepository.existsByUsername(user.getUsername())) {
			
			System.out.println("Username already exists");
			
			RegisterError error = new RegisterError();
			error.setError("User already exist with given username");
			error.setHasError(true);
			
			// reset password field
			user.setPassword(null);

			model.addAttribute("user", user);
			model.addAttribute("error", error);
			return "register.html";
			
		}

		userRepository.save(user);

		return "redirect:/";

	}

}
