package com.aiu.library.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowID;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_status")
    private Boolean returnStatus = false;

    public BorrowRecord() {}

    public BorrowRecord(Integer borrowID, Member member, Book book,
                        LocalDate borrowDate, LocalDate dueDate, Boolean returnStatus) {
        this.borrowID = borrowID;
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnStatus = returnStatus;
    }

    public Integer getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(Integer borrowID) {
        this.borrowID = borrowID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(Boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

    public Integer getMemberId() {
        return member != null ? member.getMemberId() : null;
    }

    public Integer getBookId() {
        return book != null ? book.getBookID() : null;
    }

    public void issueBook(Member member, Book book, int loanDays) {
        this.member = member;
        this.book = book;
        this.borrowDate = LocalDate.now();
        this.dueDate = this.borrowDate.plusDays(loanDays);
        this.returnStatus = false;
    }

    public void returnBook() {
        this.returnStatus = true;
    }

    public void renewBook(int extraDays) {
        if (this.dueDate == null) this.dueDate = LocalDate.now();
        this.dueDate = this.dueDate.plusDays(extraDays);
    }
}
