package com.aiu.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;

    @Column(name = "name")
    private String name ;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "membership_date")
    private String membershipDate;
    
    public Member() {}

    public Member(Integer memberId, String name, String contactInfo,
                  String membershipDate) {
        this.memberId = memberId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.membershipDate = membershipDate;
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
    

}
