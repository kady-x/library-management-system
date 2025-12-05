
package com.aiu.library.model;

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



