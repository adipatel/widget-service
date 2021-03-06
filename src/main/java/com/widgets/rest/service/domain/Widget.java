package com.widgets.rest.service.domain;

import java.time.Instant;
import java.util.UUID;

public class Widget {
	private final UUID widgetId;
	private Integer xCoordinate;
	private Integer yCoordinate;
	private Integer width;
	private Integer height;
	private Integer zIndex;
	private Instant lasModified;

	public Widget(Integer xCoordinate,
				  Integer yCoordinate,
				  Integer width,
				  Integer height,
				  Integer zIndex) {
		this.widgetId = UUID.randomUUID();
		setXCoordinate(xCoordinate);
		setYCoordinate(yCoordinate);
		setWidth(width);
		setHeight(height);
		setZIndex(zIndex);
		setLasModified(Instant.now());
	}

	public void setXCoordinate(Integer xCoordinate) { this.xCoordinate = xCoordinate; }

	public void setYCoordinate(Integer yCoordinate) { this.yCoordinate = yCoordinate; }

	public void setWidth(Integer width) { this.width = width; }

	public void setHeight(Integer height) { this.height = height; }

	public void setLasModified(Instant lasModified) { this.lasModified = lasModified; }

	public void setZIndex(Integer zIndex) { this.zIndex = zIndex; }

	public UUID getWidgetId() { return widgetId; }

	public Integer getXCoordinate() { return xCoordinate; }

	public Integer getYCoordinate() { return yCoordinate; }

	public Integer getWidth() { return width; }

	public Integer getHeight() { return height; }

	public Integer getZIndex() { return zIndex; }

	public Instant getLasModified() { return lasModified; }
}
