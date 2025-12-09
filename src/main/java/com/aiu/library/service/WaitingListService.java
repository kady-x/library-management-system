// src/main/java/com/aiu/library/service/WaitingListService.java
package com.aiu.library.service;

import com.aiu.library.datastructures.WaitingQueue;
import com.aiu.library.model.Member;
import com.aiu.library.model.WaitingListEntry;
import com.aiu.library.repository.BookRepository;
import com.aiu.library.repository.MemberRepository;
import com.aiu.library.repository.WaitingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaitingListService {

    @Autowired private WaitingListRepository waitingListRepo;
    @Autowired private BookRepository bookRepo;
    @Autowired private MemberRepository memberRepo;

    public String joinWaitingList(int bookId, int memberId) {
        Member member = memberRepo.findById(memberId);
        if (member == null) return "Member not found!";

        if (bookRepo.findById(bookId) != null) {
            return "Book is available! You can borrow it now.";
        }

        WaitingListEntry entry = new WaitingListEntry(memberId, member.getName());
        WaitingQueue queue = waitingListRepo.getQueueForBook(bookId);
        queue.enqueue(entry);

        return "Added to waiting list. Your position: " + queue.size();
    }

    public WaitingListEntry getNextInLine(int bookId) {
        WaitingQueue queue = waitingListRepo.getQueueForBook(bookId);
        return queue.peek();
    }

    public void notifyAndRemoveNext(int bookId) {
        WaitingListEntry next = getNextInLine(bookId);
        if (next != null) {
            waitingListRepo.getQueueForBook(bookId).dequeue();
            System.out.println("Book available for: " + next.getMemberName() + " (ID: " + next.getMemberId() + ")");
        }
    }

    public WaitingQueue getWaitingList(int bookId) {
        return waitingListRepo.getQueueForBook(bookId);
    }
}
