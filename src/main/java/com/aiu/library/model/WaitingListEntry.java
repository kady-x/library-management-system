package com.aiu.library.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "waiting_list")
public class WaitingListEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;

    public WaitingListEntry() {}

    public WaitingListEntry(Long id, Book book, Member member) {
        this.id = id;
        this.book = book;
        this.member = member;
    }
}
