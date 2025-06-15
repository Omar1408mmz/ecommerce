package com.artere.ecommerce.dto;

import java.util.List;

/**
 * A generic DTO for paginated responses.
 * @param <T> The type of content in the page
 */
public record PageDTO<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
) {
    /**
     * Creates a PageDTO from a Spring Data Page object.
     * @param page The Spring Data Page object
     * @param <T> The type of content in the page
     * @return A new PageDTO instance
     */
    public static <T> PageDTO<T> fromPage(org.springframework.data.domain.Page<T> page) {
        return new PageDTO<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast()
        );
    }
}