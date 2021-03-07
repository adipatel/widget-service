package com.widgets.rest.service.controllers;

import com.widgets.rest.service.WidgetServiceApp;
import com.widgets.rest.service.controllers.dto.WidgetCreateDto;
import com.widgets.rest.service.controllers.dto.WidgetReadDto;
import com.widgets.rest.service.controllers.dto.WidgetUpdateDto;
import com.widgets.rest.service.domain.Widget;
import com.widgets.rest.service.repositories.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(
		value = "/api/widgets",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
)
public class WidgetController {

	private static Logger logger = LoggerFactory.getLogger(WidgetController.class.getSimpleName());
	private final WidgetRepository widgetRepository;

	public WidgetController(WidgetRepository widgetRepository) {
		this.widgetRepository = widgetRepository;
	}

	@GetMapping
	public ResponseEntity<Collection<WidgetReadDto>> listAllWidgets() {
		return ResponseEntity.ok(widgetRepository.getAllWidgets());
	}

	@GetMapping("{id}")
	public ResponseEntity<WidgetReadDto> getWidgetById(@PathVariable("id") UUID widgetId) {
		return ResponseEntity.ok(widgetRepository.getWidget(widgetId));
	}

	@PostMapping
	public ResponseEntity<WidgetReadDto> createWidget(@Valid @RequestBody WidgetCreateDto widgetCreateDto) {
		Optional<String> validationErrors = widgetCreateDto.validationErrors();
		if (validationErrors.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationErrors.get());
		return ResponseEntity.ok(widgetRepository.createWidget(widgetCreateDto));
	}

	@PatchMapping("{id}")
	public ResponseEntity<WidgetReadDto> updateWidgetById(@PathVariable("id") @Valid  UUID widgetId,
												   @Valid @RequestBody WidgetUpdateDto widgetUpdateDto) {
		Optional<String> validationErrors = widgetUpdateDto.validationErrors();
		if (validationErrors.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationErrors.get());
		return ResponseEntity.ok(widgetRepository.updateWidget(widgetId, widgetUpdateDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteWidgetById(@PathVariable("id") UUID widgetId) {
		return ResponseEntity.ok(widgetRepository.deleteWidget(widgetId));
	}
}
