package com.widgets.rest.service.repositories;

import com.widgets.rest.service.controllers.dto.WidgetCreateDto;
import com.widgets.rest.service.controllers.dto.WidgetReadDto;
import com.widgets.rest.service.controllers.dto.WidgetUpdateDto;
import com.widgets.rest.service.domain.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Repository
public class WidgetRepository {
    private static Logger logger = LoggerFactory.getLogger(WidgetRepository.class.getSimpleName());
    private static ReentrantReadWriteLock widgetsLock = new ReentrantReadWriteLock();
    private final Map<UUID, Widget> widgetStore = new HashMap<UUID, Widget>();

    public WidgetRepository() {
    }

    public WidgetReadDto createWidget(WidgetCreateDto widgetCreateDto) {
        widgetsLock.writeLock().lock();
        try {
            Widget newWidget = new Widget(widgetCreateDto);
            widgetStore.put(newWidget.getWidgetId(), newWidget);
            return new WidgetReadDto(newWidget);
        } finally {
            widgetsLock.writeLock().unlock();
        }
    }

    public Void deleteWidget(UUID widgetId) {
        widgetsLock.writeLock().lock();
        try {
            if (null == widgetStore.remove(widgetId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } finally {
            widgetsLock.writeLock().unlock();
        }
        return null;
    }

    public WidgetReadDto updateWidget(UUID widgetId, WidgetUpdateDto widgetUpdateDto) {
        widgetsLock.writeLock().lock();
        try {
            Widget widgetToUpdate = widgetStore.get(widgetId);
            if (null == widgetToUpdate) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            if (widgetUpdateDto.getX().isPresent())      widgetToUpdate.setXCoordinate(widgetUpdateDto.getX().get());
            if (widgetUpdateDto.getY().isPresent())      widgetToUpdate.setYCoordinate(widgetUpdateDto.getY().get());
            if (widgetUpdateDto.getWidth().isPresent())  widgetToUpdate.setWidth(widgetUpdateDto.getWidth().get());
            if (widgetUpdateDto.getHeight().isPresent()) widgetToUpdate.setHeight(widgetUpdateDto.getHeight().get());
            if (widgetUpdateDto.getZ().isPresent())      widgetToUpdate.setZIndex(widgetUpdateDto.getZ().get());
            widgetToUpdate.setLasModified(Instant.now());
            widgetStore.put(widgetId, widgetToUpdate);
            return new WidgetReadDto(widgetToUpdate);
        } finally {
            widgetsLock.writeLock().unlock();
        }
    }

    public WidgetReadDto getWidget(UUID widgetId) {
        widgetsLock.readLock().lock();
        try {
            Widget response = widgetStore.get(widgetId);
            if (null == response) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return new WidgetReadDto(response);
        } finally {
            widgetsLock.readLock().unlock();
        }
    }

    public Collection<WidgetReadDto> getAllWidgets() {
        widgetsLock.readLock().lock();
        try {
            return widgetStore.values().stream()
                    .map(w -> new WidgetReadDto(w))
                    .collect(Collectors.toUnmodifiableList());
        } finally {
            widgetsLock.readLock().unlock();
        }
    }
}
