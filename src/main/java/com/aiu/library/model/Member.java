package com.aiu.library.model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private Integer memberId;
    private String name ;
    private String contactInfo;
    private String membershipDate;
    private List<Book> borrowedBooks = new ArrayList<>();
    
    public Integer getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public String getMembershipDate() {
        return membershipDate;
    }
    
    public void setMembershipDate(String membershipDate) {
        this.membershipDate = membershipDate;
    }
    
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    
    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
