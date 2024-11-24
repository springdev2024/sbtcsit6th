package com.example.sbtcsit6th;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/home")
	public String home() {
		return "Hello World";
	}
	
//	@GetMapping("/time")
//	public String time() {
//		return "Current time is " + (new Date());
//	}
	
}
