package com.widgets.rest.service.controllers.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WidgetFilterDto {
    private final Integer x1;
    private final Integer y1;
    private final Integer x2;
    private final Integer y2;

    public WidgetFilterDto(Integer x1, Integer y1, Integer x2, Integer y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Integer getX1() {
        return x1;
    }

    public Integer getY1() {
        return y1;
    }

    public Integer getX2() {
        return x2;
    }

    public Integer getY2() {
        return y2;
    }

    public boolean isValid() {
        if (null == x1 && null == y1 && null == x2 && null == y2) return false;
        if (null == x1 || null == y1 || null == x2 || null == y2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filter coordinates(if set) all must be be non null.");
        if (!((x2-x1) > 0 && (y2-y1) > 0))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Rectangle represented by (%s,%s) & (%s,%s) isn't valid", x1, y1, x2, y2));
        return true;
    }
}
