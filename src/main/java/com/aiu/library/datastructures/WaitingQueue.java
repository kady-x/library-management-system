package com.aiu.library.datastructures;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import com.aiu.library.model.WaitingListEntry;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.Iterator;
import org.slf4j.Logger;
import java.util.List;

public class WaitingQueue {

	private static final Logger logger = LoggerFactory.getLogger(WaitingQueue.class);
	private final LinkedList<WaitingListEntry> list = new LinkedList<>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public WaitingQueue() {
		logger.debug("WaitingQueue initialized");
	}

	public void enqueue(WaitingListEntry entry) {
		if (entry == null) {
			logger.warn("Attempted to enqueue null entry - ignoring");
			return;
		}

		lock.writeLock().lock();
		try {
			list.addLast(entry);
			logger.debug("Enqueued waiting list entry with ID: {}", entry.getId());
		} finally {
			lock.writeLock().unlock();
		}
	}

	public WaitingListEntry dequeue() {
		lock.writeLock().lock();
		try {
			WaitingListEntry entry = list.isEmpty() ? null : list.removeFirst();
			if (entry != null) {
				logger.debug("Dequeued waiting list entry with ID: {}", entry.getId());
			} else {
				logger.debug("Attempted to dequeue from empty queue");
			}
			return entry;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public WaitingListEntry peek() {
		lock.readLock().lock();
		try {
			return list.peekFirst();
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return list.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	public int size() {
		lock.readLock().lock();
		try {
			return list.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	public List<WaitingListEntry> toList() {
		lock.readLock().lock();
		try {
			List<WaitingListEntry> copy = new LinkedList<>(list);
			logger.debug("Created copy of queue with {} entries", copy.size());
			return copy;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean removeById(Integer id) {
		if (id == null) {
			logger.warn("Attempted to remove by null ID");
			return false;
		}

		lock.writeLock().lock();
		try {
			Iterator<WaitingListEntry> it = list.iterator();
			while (it.hasNext()) {
				WaitingListEntry e = it.next();
				if (e != null && id.equals(e.getWaitingListID())) {
					it.remove();
					logger.debug("Removed waiting list entry with ID: {}", id);
					return true;
				}
			}
			logger.debug("Could not find waiting list entry with ID: {}", id);
			return false;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public int removeAllByMemberId(Integer memberId) {
		if (memberId == null) {
			logger.warn("Attempted to remove by null member ID");
			return 0;
		}

		lock.writeLock().lock();
		try {
			int removed = 0;
			Iterator<WaitingListEntry> it = list.iterator();
			while (it.hasNext()) {
				WaitingListEntry e = it.next();
				if (e != null && e.getMember() != null && memberId.equals(e.getMember().getMemberId())) {
					it.remove();
					removed++;
					logger.debug("Removed waiting list entry for member ID: {}", memberId);
				}
			}
			logger.debug("Removed {} entries for member ID: {}", removed, memberId);
			return removed;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public List<WaitingListEntry> findByBookId(Integer bookId) {
		if (bookId == null) {
			logger.warn("Attempted to find by null book ID");
			return new LinkedList<>();
		}

		lock.readLock().lock();
		try {
			LinkedList<WaitingListEntry> out = new LinkedList<>();
			for (WaitingListEntry e : list) {
				if (e != null && e.getBook() != null && bookId.equals(e.getBook().getBookID())) {
					out.add(e);
				}
			}
			logger.debug("Found {} entries for book ID: {}", out.size(), bookId);
			return out;
		} finally {
			lock.readLock().unlock();
		}
	}
}
