package com.example.sbtcsit6th.todo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

	@Autowired
	private TodoRepository todoRepository;

	@GetMapping("/todo")
	public String getTodo(Model model) {
		model.addAttribute("todos", todoRepository.findAll());
		return "todo";
	}

	@PostMapping("/todo/new")
	public String createNewTodo(@RequestParam("task") String task, @RequestParam("description") String description,
			Model model) {

		Todo todo = new Todo();
		todo.setTask(task);
		todo.setDescription(description);
		todo.setCompleted(false);
		todo.setCreatedAt(LocalDateTime.now());

		todoRepository.save(todo);

		model.addAttribute("todos", todoRepository.findAll());
		return "todo";
	}

	@GetMapping("/todo/delete/{id}")
	public String deleteTodo(@PathVariable("id") int id, Model model) {

		todoRepository.deleteById(id);

		model.addAttribute("todos", todoRepository.findAll());

		return "todo";
	}

	@GetMapping("/todo/update/{id}")
	public String updateTodo(@PathVariable("id") int id, @RequestParam("task") String task,
			@RequestParam("description") String description, Model model) {

		Optional<Todo> optionalTodo = todoRepository.findById(id);
		
		if(optionalTodo.isPresent()) {
			Todo todo = optionalTodo.get();
			todo.setTask(task);
			todo.setDescription(description);
			todoRepository.save(todo);
		}

		model.addAttribute("todos", todoRepository.findAll());
		return "todo";
	}

}
