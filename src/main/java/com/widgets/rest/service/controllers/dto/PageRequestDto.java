package com.widgets.rest.service.controllers.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

public class PageRequestDto {
    private final Integer startKey;
    @Max(500)
    @Positive
    private Integer pageSize = 10;

    public PageRequestDto(Integer startKey, Integer pageSize) {
        this.pageSize = pageSize;
        this.startKey = startKey;
    }

    public Integer getStartKey() {
        return startKey;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageSizeSafe() {
        return (null == pageSize ? 10 : pageSize);
    }
}
