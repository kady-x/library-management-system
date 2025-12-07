
package com.aiu.library.model;
<<<<<<< HEAD
=======

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
>>>>>>> 04729fb8a1955cfaf27e62931dbd4c83b169ca81

import java.time.LocalDate;

public class WaitingListEntry {
    private final Long memberId;
    private final String memberName;
    private final LocalDate addedDate;

    public WaitingListEntry(Long memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.addedDate = LocalDate.now();
    }

    public Long getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public LocalDate getAddedDate() { return addedDate; }

    @Override
    public String toString() {
        return memberName + " (ID: " + memberId + ") - Joined: " + addedDate;
    }
}



