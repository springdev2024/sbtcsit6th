package com.example.sbtcsit6th.todo;

import java.util.ArrayList;
import java.util.List;

public class TodoService {

	private static List<Todo> todoList = new ArrayList<>();

	public static List<Todo> getAll() {
		return todoList;
	}

	public static void add(String task, String description) {
		todoList.add(new Todo(task, description));
	}

	public static void delete(int id) {
		todoList.removeIf(todo -> todo.getId() == id);
	}

}
