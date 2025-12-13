package com.aiu.library.model;

public class MemberWithBillingRequest {
    private String name;
    private String contactInfo;
    private String membershipDate;
    private Double totalFines;
    private String paymentHistoryText;
    
    public MemberWithBillingRequest() {}
    
    public MemberWithBillingRequest(String name, String contactInfo, String membershipDate, 
                                  Double totalFines, String paymentHistoryText) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.membershipDate = membershipDate;
        this.totalFines = totalFines;
        this.paymentHistoryText = paymentHistoryText;
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
    
    public Double getTotalFines() {
        return totalFines;
    }
    
    public void setTotalFines(Double totalFines) {
        this.totalFines = totalFines;
    }
    
    public String getPaymentHistoryText() {
        return paymentHistoryText;
    }
    
    public void setPaymentHistoryText(String paymentHistoryText) {
        this.paymentHistoryText = paymentHistoryText;
    }
    
    public boolean hasBillingInfo() {
        return (totalFines != null && totalFines > 0) || 
               (paymentHistoryText != null && !paymentHistoryText.trim().isEmpty());
    }
}
