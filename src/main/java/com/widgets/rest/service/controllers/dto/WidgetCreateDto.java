package com.widgets.rest.service.controllers.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.Positive;
import java.util.Optional;

public class WidgetCreateDto {
    private Integer x;
    private Integer y;
    private JsonNullable<Integer> z = JsonNullable.undefined();
    @Positive private Integer width;
    @Positive private Integer height;

    public Integer getX() { return x; }

    public Integer getY() { return y; }

    public JsonNullable<Integer> getZ() { return z; }

    public Integer getWidth() { return width; }

    public Integer getHeight() { return height; }

    public Optional<String> validationErrors() {
        Optional<String> response = Optional.empty();
        if (x == null)           response = Optional.of("x cannot be null");
        else if (y == null)      response = Optional.of("y cannot be null");
        else if (isNull(z))      response = Optional.of("z cannot be null");
        else if (width == null)  response = Optional.of("width cannot be null");
        else if (height == null) response = Optional.of("height cannot be null");

        return response;
    }

    private Boolean isNull(JsonNullable<Integer> obj) { return (obj.isPresent() && obj.get() == null); }
}
