package com.widgets.rest.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class WidgetController {

	@GetMapping("/widgets")
	public List<Widget> listWidgets(@RequestParam(value = "name", defaultValue = "World") String name) {
		return List.of(
				new Widget(UUID.randomUUID(), name),
				new Widget(UUID.randomUUID(), name)
		);
	}

	@GetMapping(value="/widget/{id}")
	public Widget widgetById(@PathVariable("id") UUID id,
							@RequestParam(value = "name", defaultValue = "A Widget") String name) {
		return new Widget(id, name);
	}
}
