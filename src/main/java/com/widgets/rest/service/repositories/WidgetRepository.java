package com.widgets.rest.service.repositories;

import com.widgets.rest.service.controllers.dto.WidgetCreateDto;
import com.widgets.rest.service.controllers.dto.WidgetReadDto;
import com.widgets.rest.service.controllers.dto.WidgetUpdateDto;
import com.widgets.rest.service.domain.Widget;
import org.openapitools.jackson.nullable.JsonNullable;
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
    private final SortedMap<Integer, UUID> zIndexStore = new TreeMap<Integer, UUID>();

    public WidgetRepository() {
    }

    public WidgetReadDto createWidget(WidgetCreateDto widgetCreateDto) {
        widgetsLock.writeLock().lock();
        try {
            Widget newWidget = new Widget(widgetCreateDto);
            newWidget.setZIndex(computeZ(newWidget.getWidgetId(), widgetCreateDto.getZ()));
            widgetStore.put(newWidget.getWidgetId(), newWidget);
            return new WidgetReadDto(newWidget);
        } finally {
            widgetsLock.writeLock().unlock();
        }
    }

    public Void deleteWidget(UUID widgetId) {
        widgetsLock.writeLock().lock();
        try {
            Widget widgetToRemove = widgetStore.get(widgetId);
            if (null == widgetToRemove) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            zIndexStore.remove(widgetToRemove.getZIndex());
            widgetStore.remove(widgetId);
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
            if (widgetUpdateDto.getZ().isPresent()) {
                zIndexStore.remove(widgetToUpdate.getZIndex());
                widgetToUpdate.setZIndex(widgetUpdateDto.getZ().get());
                updateZIndexes(widgetId, widgetUpdateDto.getZ().get());
            }
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
            return zIndexStore.keySet().stream()
                    .map(z -> new WidgetReadDto(widgetStore.get(zIndexStore.get(z))))
                    .collect(Collectors.toUnmodifiableList());
        } finally {
            widgetsLock.readLock().unlock();
        }
    }

    private Integer computeZ(UUID widgetId, JsonNullable<Integer> z) {
        if (z.isPresent()) {
            updateZIndexes(widgetId, z.get());
            return z.get();
        } else {
            Integer newZIndex = (zIndexStore.isEmpty()) ? 1 : (zIndexStore.lastKey() + 1);
            zIndexStore.put(newZIndex, widgetId);
            return newZIndex;
        }
    }

    private Void updateZIndexes(UUID widgetId, Integer newZIndex) {
        UUID widgetToMove = zIndexStore.put(newZIndex, widgetId);
        while (widgetToMove != null) {
            newZIndex += 1;
            Widget widgetToUpdate = widgetStore.get(widgetToMove);
            widgetToUpdate.setZIndex(newZIndex);
            widgetStore.put(widgetToMove, widgetToUpdate);
            widgetToMove = zIndexStore.put(newZIndex, widgetToMove);
        }
        return null;
    }
}
