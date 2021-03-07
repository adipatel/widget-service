package com.widgets.rest.service.repositories;

import com.widgets.rest.service.controllers.dto.*;
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
    private final SortedSet<Widget> rectangleStore = new TreeSet<Widget>(new Widget.ByCoordinatesWithoutZIndex());

    public WidgetRepository() {
    }

    public WidgetReadDto createWidget(WidgetCreateDto widgetCreateDto) {
        widgetsLock.writeLock().lock();
        try {
            Widget newWidget = new Widget(widgetCreateDto);
            Integer zIndex = computeZ(newWidget.getWidgetId(), widgetCreateDto.getZ());
            logger.info(String.format("Assigning zIndex=%s to Widget with Id:%s", zIndex, newWidget.getWidgetId()));
            newWidget.setZIndex(zIndex);
            widgetStore.put(newWidget.getWidgetId(), newWidget);
            rectangleStore.add(newWidget);
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
            rectangleStore.remove(widgetToRemove);
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
            rectangleStore.remove(widgetToUpdate);
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
            rectangleStore.add(widgetToUpdate);
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

    public WidgetPageDto getAllWidgets(PageRequestDto pageRequest) {
        widgetsLock.readLock().lock();
        try {
            return readPage(pageRequest);
        } finally {
            widgetsLock.readLock().unlock();
        }
    }

    public Collection<WidgetReadDto> filterWidgets(WidgetFilterDto widgetFilterDto) {
        widgetsLock.readLock().lock();
        try {
            return findOverlapping(widgetFilterDto);
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


    // Not updating rectangleStore as only ZIndex is being updated for Widgets
    private Void updateZIndexes(UUID widgetId, Integer newZIndex) {
        UUID widgetToMove = zIndexStore.put(newZIndex, widgetId);
        while (widgetToMove != null) {
            logger.info(String.format("Updating zIndex to %s for Widget with Id:%s", newZIndex, widgetToMove));
            newZIndex += 1;
            Widget widgetToUpdate = widgetStore.get(widgetToMove);
            widgetToUpdate.setZIndex(newZIndex);
            widgetStore.put(widgetToMove, widgetToUpdate);
            widgetToMove = zIndexStore.put(newZIndex, widgetToMove);
        }
        return null;
    }

    private WidgetPageDto readPage(PageRequestDto pageRequest) {
        if (zIndexStore.isEmpty()) return new WidgetPageDto(pageRequest.getPageSizeSafe());
        Integer elementsToReturn = pageRequest.getPageSizeSafe();
        Integer startZIndex =  (pageRequest.getStartKey() == null) ? zIndexStore.firstKey() : pageRequest.getStartKey();

        List<WidgetReadDto> results = new ArrayList<>(elementsToReturn+1);
        int n = 0;
        Iterator<Integer> iter = zIndexStore.tailMap(startZIndex).keySet().iterator();
        while (n < elementsToReturn  && iter.hasNext()) {
            results.add(new WidgetReadDto(widgetStore.get(zIndexStore.get(iter.next()))));
            n++;
        }
        Integer nextKey = iter.hasNext() ? iter.next() : null;
        return new WidgetPageDto(results, nextKey, elementsToReturn);
    }

    private Collection<WidgetReadDto> findOverlapping(WidgetFilterDto widgetFilterDto) {
        if (!widgetFilterDto.isValid()) return Collections.emptyList();
        Widget bottomLeft = new Widget(widgetFilterDto.getX1(), widgetFilterDto.getY1(), 0, 0);
        Widget topRight = new Widget(widgetFilterDto.getX2()+1, widgetFilterDto.getY1()+1, 0, 0);
        return rectangleStore.subSet(bottomLeft, topRight)
                .stream()
                .filter(w -> w.overlaps(widgetFilterDto))
                .map(w -> new WidgetReadDto(w))
                .collect(Collectors.toList());
    }
}
