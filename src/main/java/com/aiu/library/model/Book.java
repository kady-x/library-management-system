package com.aiu.library.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookID;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "availability_status")
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