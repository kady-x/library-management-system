package com.aiu.library.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;
    private String name ;
    private String contactInfo;
    private String membershipDate;
    
    @OneToMany
    private List<Book> borrowedBooks = new ArrayList<>();
    
    public Member() {}

    public Member(Integer memberId, String name, String contactInfo,
                  String membershipDate, List<Book> borrowedBooks) {
        this.memberId = memberId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.membershipDate = membershipDate;
        this.borrowedBooks = new ArrayList<>(borrowedBooks);
    }

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
