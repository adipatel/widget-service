package com.widgets.rest.service;

import java.util.UUID;

public class Widget {

	private final UUID id;
	private final String name;

	public Widget(UUID id, String content) {
		this.id = id;
		this.name = content;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
