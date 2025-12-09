
package com.aiu.library.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "waitinglist")
public class WaitingListEntry {

    
    @Id
    private final int waitinglistID;
    private final int memberId;
    private final String memberName;
    private final LocalDate addedDate;

    public WaitingListEntry(int waitinglistID, int memberId, String memberName) {
        this.waitinglistID = waitinglistID;
        this.memberId = memberId;
        this.memberName = memberName;
        this.addedDate = LocalDate.now();
    }

    public int getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public LocalDate getAddedDate() { return addedDate; }

    @Override
    public String toString() {
        return memberName + " (ID: " + memberId + ") - Joined: " + addedDate;
    }
}



