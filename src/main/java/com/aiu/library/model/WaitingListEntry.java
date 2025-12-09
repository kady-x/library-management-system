package com.aiu.library.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

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

    public WaitingListEntry() {}

    public WaitingListEntry(Integer id, Book book, Member member) {
        this.id = id;
        this.book = book;
        this.member = member;
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

    public Integer getWaitingListID() {
        return id;
    }
}