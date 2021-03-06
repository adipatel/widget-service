package com.widgets.rest.service.controllers.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.Positive;
import java.util.Optional;

public class WidgetUpdateDto {
    private JsonNullable<Integer> x = JsonNullable.undefined();
    private JsonNullable<Integer> y = JsonNullable.undefined();
    private JsonNullable<Integer> z = JsonNullable.undefined();
    @Positive
    private JsonNullable<Integer> width = JsonNullable.undefined();
    @Positive
    private JsonNullable<Integer> height = JsonNullable.undefined();

    protected WidgetUpdateDto() {}

    public JsonNullable<Integer> getX() { return x; }

    public JsonNullable<Integer> getY() { return y; }

    public JsonNullable<Integer> getZ() { return z; }

    public JsonNullable<Integer> getWidth() { return width; }

    public JsonNullable<Integer> getHeight() { return height; }

    public Optional<String> validationErrors() {
        Optional<String> response = Optional.empty();

        if (isNull(x))           response = Optional.of("x cannot be null");
        else if (isNull(y))      response = Optional.of("y cannot be null");
        else if (isNull(z))      response = Optional.of("z cannot be null");
        else if (isNull(width))  response = Optional.of("width cannot be null");
        else if (isNull(height)) response = Optional.of("height cannot be null");

        return response;
    }

    private Boolean isNull(JsonNullable<Integer> obj) { return (obj.isPresent() && obj.get() == null); }
}
