package com.example.sbtcsit6th.todo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TodoController {

	@Autowired
	private TodoRepository todoRepository;

	public void setCookieForRequest(HttpServletResponse response) {
		Cookie cookie = new Cookie("token", new Random().nextInt() + "");
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
	}

	public Cookie getCookieFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Cookie foundCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("token")) {
					foundCookie = cookie;
				}
				System.out.printf(String.format("%s -> %s\n", cookie.getName(), cookie.getValue()));
			}
		}
		return foundCookie;
	}

	@GetMapping("/login")
	public String doLoginAndGetTodoPage(HttpServletResponse response, HttpServletRequest request) {
		Cookie cookie = getCookieFromRequest(request);
		if (cookie == null) {
			setCookieForRequest(response);
		}
		return "redirect:/todo";
	}

	@GetMapping("/todo")
	public String getTodo(Model model, HttpServletRequest request) {

		Cookie cookie = getCookieFromRequest(request);
		String session = "";

		if (cookie != null) {
			session = cookie.getValue();
		}

		model.addAttribute("todos", todoRepository.findBySession(session));

		model.addAttribute("todo", new Todo());
		model.addAttribute("toCreate", true);
		return "todo";
	}

	@PostMapping("/todo/new")
	public String createNewTodo(@RequestParam("task") String task, @RequestParam("description") String description,
			Model model, HttpServletRequest request) {

		Cookie cookie = getCookieFromRequest(request);

		if (cookie != null) {
			Todo todo = new Todo();
			todo.setTask(task);
			todo.setDescription(description);
			todo.setCompleted(false);
			todo.setCreatedAt(LocalDateTime.now());
			todo.setSession(cookie.getValue());
			todoRepository.save(todo);
		}

		return "redirect:/todo";
	}

	@PostMapping("/todo/delete")
	public String deleteTodo(@RequestParam("todoID") int id) {

		todoRepository.deleteById(id);

		return "redirect:/todo";
	}

	@GetMapping("/todo/update/{id}")
	public String getUpdateTodoPage(@PathVariable("id") int id, Model model) {

		Optional<Todo> optionalTodo = todoRepository.findById(id);
		if (optionalTodo.isPresent()) {
			model.addAttribute("todo", optionalTodo.get());
			model.addAttribute("todos", todoRepository.findAll());
			model.addAttribute("toCreate", false);
			return "todo";
		} else {
			return "redirect:/todo";
		}

	}

	@PostMapping("/todo/update/{id}")
	public String updateTodo(@PathVariable("id") int id, @RequestParam("task") String task,
			@RequestParam("description") String description, Model model) {

		Optional<Todo> optionalTodo = todoRepository.findById(id);

		if (optionalTodo.isPresent()) {
			Todo todo = optionalTodo.get();
			todo.setTask(task);
			todo.setDescription(description);
			todoRepository.save(todo);
		}

		return "redirect:/todo";

	}

}
