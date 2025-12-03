package com.aiu.library.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordID;
    
    @ManyToOne
    private Member member;

    @ManyToOne
    private Book book;

    public BorrowRecord() {}

    public BorrowRecord(Long recordID, Member member, Book book) {
        this.recordID = recordID;
        this.member = member;
        this.book = book;
    }
}
