package com.example.sbtcsit6th.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

	@GetMapping("/todo")
	public String getTodo(Model model) {
		model.addAttribute("todos", TodoService.getAll());
		return "todo";
	}

	@GetMapping("/todo/new")
	public String createNewTodo(@RequestParam("task") String task, @RequestParam("description") String description,
			Model model) {
		TodoService.add(task, description);
		model.addAttribute("todos", TodoService.getAll());
		return "todo";
	}

	@GetMapping("/todo/delete/{id}")
	public String deleteTodo(@PathVariable("id") int id, Model model) {
		TodoService.delete(id);
		model.addAttribute("todos", TodoService.getAll());
		return "todo";
	}

//	@GetMapping("/todo/new/{task}")
//	public String createNewTodo(@PathVariable("task") String task, Model model) {
//		TodoService.add(task);
//		model.addAttribute("todos", TodoService.getAll());
//		return "todo";
//	}

}
