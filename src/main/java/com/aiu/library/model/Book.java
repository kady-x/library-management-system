package com.aiu.library.model;

public class Book {
    private Integer bookID;
    private String title;
    private String author;
    private String genre;
    private Integer publicationYear;
    private Boolean availabilityStatus;

    public Book(Integer bookID, String title, String author, String genre, Integer publicationYear, Boolean availabilityStatus) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.availabilityStatus = availabilityStatus;
    }

    public Integer getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Boolean getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setAvailabilityStatus(Boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}