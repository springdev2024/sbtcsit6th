package com.example.sbtcsit6th;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoController {

	@GetMapping
	public String getTodo() {
		return "todo";
	}

}