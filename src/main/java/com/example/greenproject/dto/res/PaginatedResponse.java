package com.example.greenproject.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class PaginatedResponse<T> implements Serializable {
    private List<T> content;
    private int totalPages;
    private int currentPage;
    private long totalElements;

    public PaginatedResponse(List<T> content, int totalPages, int currentPage, long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
    }
}
