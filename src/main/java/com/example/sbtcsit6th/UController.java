package com.example.sbtcsit6th;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UController {

	@GetMapping("/time")
	public String time(Model model) {
		model.addAttribute("datetime", new Date());
		return "time";
	}
	
}
