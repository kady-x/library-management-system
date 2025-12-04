package com.aiu.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    private Integer bookID;
    private String title;
    private String author;
    private String genre;
    private String coverUrl;
    private Integer publicationYear;
    private Boolean availabilityStatus;
    
    public Book() {}

    public Book(Integer bookID, String title, String author, String genre,
                String coverUrl, Integer publicationYear, Boolean availabilityStatus) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.coverUrl = coverUrl;
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

    public String getCoverUrl() {return this.coverUrl;}

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Boolean getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public void setCoverUrl(String coverUrl) {this.coverUrl = coverUrl;}

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