package com.example.web_2.util;

public class PaginationValidator {
    public static int validatePagination(int pageNumber, int pageSize, long elementsCount) {
        if (pageSize <= 0 || pageSize > 50) {
            throw new IllegalArgumentException(
                    String.format("Page size: %d is incorrect, value must be positive number lower than 50",
                            pageSize));
        }
        int totalPages = (int) elementsCount / pageSize + (elementsCount % pageSize == 0 ? 0 : 1);
        if (pageNumber > totalPages || pageNumber <= 0) {
            throw new IllegalArgumentException(
                    String.format("Page number: %d is incorrect, current available number of pages: 1-%d",
                            pageNumber, totalPages));
        }
        return totalPages;
    }
}
