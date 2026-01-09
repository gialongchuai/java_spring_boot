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
    private T items; // dùng kiểu T vis dụ
    // truyền gì cũng được ví dụ List user, sau này là list Post ....
}
