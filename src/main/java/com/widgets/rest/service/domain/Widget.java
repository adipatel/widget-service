package com.widgets.rest.service.domain;

import com.widgets.rest.service.controllers.dto.WidgetCreateDto;
import com.widgets.rest.service.controllers.dto.WidgetFilterDto;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public class Widget {
	private final UUID widgetId;
	private Integer xCoordinate;
	private Integer yCoordinate;
	private Integer width;
	private Integer height;
	private Integer zIndex;
	private Instant lasModified;

	public Widget(WidgetCreateDto widgetCreateDto) {
		this.widgetId = UUID.randomUUID();
		setXCoordinate(widgetCreateDto.getX());
		setYCoordinate(widgetCreateDto.getY());
		setWidth(widgetCreateDto.getWidth());
		setHeight(widgetCreateDto.getHeight());
		setLasModified(Instant.now());
	}

	public Widget(Integer x, Integer y, Integer width, Integer height) {
		this.widgetId = UUID.randomUUID();
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.width = width;
		this.height = height;
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

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Widget)) {
			return false;
		}
		Widget widget = (Widget) o;
		return widgetId == widget.getWidgetId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(widgetId);
	}

	public static class ByCoordinatesWithoutZIndex implements Comparator<Widget> {
		@Override
		public int compare(Widget o1, Widget o2) {
			if (o1.widgetId.equals(o2.widgetId)) return 0;
			else if (o1.xCoordinate < o2.xCoordinate) return -1;
			else if (o1.xCoordinate > o2.xCoordinate) return 1;
			else if (o1.yCoordinate < o2.yCoordinate) return -1;
			else if (o1.yCoordinate > o2.yCoordinate) return 1;
			else if (o1.width < o2.width) return -1;
			else if (o1.width > o2.width) return 1;
			else if (o1.height < o2.height) return -1;
			else if (o1.height > o2.height) return 1;
			return o1.widgetId.compareTo(o2.widgetId);
		}
	}

	/**
	 * For finding an overlap assuming that (xCoordinate,yCoordinate) represents the bottomLeft corner
	 * (xCoordinate+width,yCoordinate+height) represents the topRight corner of the Widget.
 	 */
	public boolean overlaps(WidgetFilterDto widgetFilterDto) {
		return (this.xCoordinate >= widgetFilterDto.getX1() &&
				this.yCoordinate >= widgetFilterDto.getY1() &&
				(this.xCoordinate + width) <= widgetFilterDto.getX2() &&
				(this.yCoordinate + height) <= widgetFilterDto.getY2());
	}


}
