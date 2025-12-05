// src/main/java/com/aiu/library/datastructure/WaitingQueue.java
package com.aiu.library.repository;

import com.aiu.library.datastructures.WaitingQueue;
import com.aiu.library.model.WaitingListEntry;
import java.util.ArrayList;
import java.util.List;

public class WaitingListRepository {
    private final List<WaitingListEntry> queue = new ArrayList<>();

    public void enqueue(WaitingListEntry entry) {
        queue.add(entry);
    }

    public WaitingListEntry dequeue() {
        if (queue.isEmpty()) return null;
        return queue.remove(0);
    }

    public WaitingListEntry peek() {
        return queue.isEmpty() ? null : queue.get(0);
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public List<WaitingListEntry> getAll() {
        return new ArrayList<>(queue);
    }

    public WaitingQueue getQueueForBook(Long bookId) {
        
        throw new UnsupportedOperationException("Unimplemented method 'getQueueForBook'");
    }
}