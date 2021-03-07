package com.widgets.rest.service.controllers.dto;

import java.util.Collection;
import java.util.Collections;

public class WidgetPageDto {
    private final Collection<WidgetReadDto> results;
    private final Integer nextStartKey;
    private final Integer requestedPageSize;


    public WidgetPageDto(Collection<WidgetReadDto> results, Integer nextKey, Integer pageSize) {
        this.results = results;
        this.nextStartKey = nextKey;
        this.requestedPageSize = pageSize;
    }

    public WidgetPageDto(Integer pageSize) {
        this.results = Collections.EMPTY_LIST;
        this.nextStartKey = null;
        this.requestedPageSize = pageSize;
    }

    public Collection<WidgetReadDto> getResults() {
        return results;
    }

    public Integer getNextStartKey() {
        return nextStartKey;
    }

    public Integer getRequestedPageSize() {
        return requestedPageSize;
    }
}
