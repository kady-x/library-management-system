package com.aiu.library.datastructures;

import com.aiu.library.model.WaitingListEntry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple FIFO queue for WaitingListEntry used by repository/service code.
 */
public class WaitingQueue {

	private final LinkedList<WaitingListEntry> list = new LinkedList<>();

	public WaitingQueue() {}

	public void enqueue(WaitingListEntry entry) {
		if (entry == null) return;
		list.addLast(entry);
	}

	public WaitingListEntry dequeue() {
		return list.isEmpty() ? null : list.removeFirst();
	}

	public WaitingListEntry peek() {
		return list.peekFirst();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public int size() {
		return list.size();
	}

	public List<WaitingListEntry> toList() {
		return new LinkedList<>(list);
	}

	public boolean removeById(Long id) {
		if (id == null) return false;
		Iterator<WaitingListEntry> it = list.iterator();
		while (it.hasNext()) {
			WaitingListEntry e = it.next();
			if (e != null && id.equals(e.getId())) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	public int removeAllByMemberId(Integer memberId) {
		if (memberId == null) return 0;
		int removed = 0;
		Iterator<WaitingListEntry> it = list.iterator();
		while (it.hasNext()) {
			WaitingListEntry e = it.next();
			if (e != null && e.getMember() != null && memberId.equals(e.getMember().getMemberId())) {
				it.remove();
				removed++;
			}
		}
		return removed;
	}

	public List<WaitingListEntry> findByBookId(Integer bookId) {
		LinkedList<WaitingListEntry> out = new LinkedList<>();
		if (bookId == null) return out;
		for (WaitingListEntry e : list) {
			if (e != null && e.getBook() != null && bookId.equals(e.getBook().getBookID())) {
				out.add(e);
			}
		}
		return out;
	}
}

   