package com.widgets.rest.service.controllers.dto;

import com.widgets.rest.service.domain.Widget;

import java.time.Instant;
import java.util.UUID;

public class WidgetReadDto {
    private final UUID widgetId;
    private final Integer xCoordinate;
    private final Integer yCoordinate;
    private final Integer width;
    private final Integer height;
    private final Integer zIndex;
    private final Instant lasModified;

    public WidgetReadDto(Widget widget) {
        widgetId = widget.getWidgetId();
        xCoordinate = widget.getXCoordinate();
        yCoordinate = widget.getYCoordinate();
        width = widget.getWidth();
        height = widget.getHeight();
        zIndex = widget.getZIndex();
        lasModified = widget.getLasModified();
    }

    public UUID getWidgetId() {
        return widgetId;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public Instant getLasModified() {
        return lasModified;
    }
}
