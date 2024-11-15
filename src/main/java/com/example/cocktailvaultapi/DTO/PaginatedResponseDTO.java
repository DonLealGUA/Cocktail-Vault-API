package com.example.cocktailvaultapi.DTO;

import java.util.List;

public class PaginatedResponseDTO <T> {
    private List<T> data;      // List of data items, in your case, CocktailDTOs
    private long totalItems;   // Total number of items in the database
    private int totalPages;    // Total number of pages based on pagination
    private int currentPage;   // Current page number
    private int pageSize;      // Number of items per page


    /**
     * Getters and setters
     */
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
