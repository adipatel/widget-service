package com.widgets.rest.service.controllers;

import com.widgets.rest.service.controllers.dto.*;
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
	public ResponseEntity<WidgetPageDto> listAllWidgets(@Valid PageRequestDto pageRequest) {
		return ResponseEntity.ok(widgetRepository.getAllWidgets(pageRequest));
	}

	@GetMapping("/filter")
	public ResponseEntity<Collection<WidgetReadDto>> filterWidgets(@Valid WidgetFilterDto widgetFilterDto) {
		return ResponseEntity.ok(widgetRepository.filterWidgets(widgetFilterDto));
	}

	@GetMapping("{id}")
	public ResponseEntity<WidgetReadDto> getWidgetById(@PathVariable("id") @Valid UUID widgetId) {
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
