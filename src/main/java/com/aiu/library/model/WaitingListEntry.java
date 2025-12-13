package com.aiu.library.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
public class WaitingListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    public WaitingListEntry() {}

    public WaitingListEntry(Integer id, Book book, Member member) {
        this.id = id;
        this.book = book;
        this.member = member;
    }

    public WaitingListEntry(Integer id, Book book, Member member, LocalDateTime requestDate) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.requestDate = requestDate;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public void setWaitingListID(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public Integer getWaitingListID() {
        return id;
    }
}