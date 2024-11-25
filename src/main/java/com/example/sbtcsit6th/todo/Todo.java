package com.example.sbtcsit6th.todo;

import java.time.LocalDateTime;

public class Todo {

	private static int currentID = 1;

	private int id;
	private String task;
	private String description;
	private LocalDateTime createdAt;

	public Todo(String task, String description) {
		this.task = task;
		this.description = description;
		this.createdAt = LocalDateTime.now();
		this.id = currentID;
		currentID++;
		System.out.println("new todo created: " + task);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
