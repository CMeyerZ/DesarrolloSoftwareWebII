package com.example.evaluacion;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String title;
    private String author;
    private Integer rating;
    private String category;
    private Integer pages;
    private String review;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private boolean favorite;
    private String tags;
    private String coverUrl;

    public Book() {}

    public Book(Long id, String title, String author, Integer rating, String category, Integer pages, String review, LocalDate startDate, LocalDate endDate, String status, boolean favorite, String tags, String coverUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.category = category;
        this.pages = pages;
        this.review = review;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.favorite = favorite;
        this.tags = tags;
        this.coverUrl = coverUrl;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}
