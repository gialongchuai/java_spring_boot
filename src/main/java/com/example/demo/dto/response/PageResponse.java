package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponse<T> {
    private int totalPages;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private T items;
}
