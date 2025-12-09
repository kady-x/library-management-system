package com.aiu.library.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aiu.library.datastructures.WaitingQueue;
import com.aiu.library.model.Book;
import com.aiu.library.model.Member;
import com.aiu.library.model.WaitingListEntry;

@Repository
public class WaitingListRepository {

	private final WaitingListJpaRepository jpaRepository;
	private final BookJpaRepository bookJpaRepository;
	private final MemberJpaRepository memberJpaRepository;
	private final WaitingQueue queue = new WaitingQueue();
	private volatile boolean initialized = false;

	public WaitingListRepository(WaitingListJpaRepository jpaRepository,
								 BookJpaRepository bookJpaRepository,
								 MemberJpaRepository memberJpaRepository) {
		this.jpaRepository = jpaRepository;
		this.bookJpaRepository = bookJpaRepository;
		this.memberJpaRepository = memberJpaRepository;
		initializeFromDatabaseIfNeeded();
	}

	public void ensureInitialized() {
		initializeFromDatabaseIfNeeded();
	}

	private synchronized void initializeFromDatabaseIfNeeded() {
		if (initialized) return;
		List<WaitingListEntry> all = null;
		try {
			all = jpaRepository.findAllByOrderByWaitingListIDAsc();
		} catch (Exception ex) {
			all = jpaRepository.findAll();
		}
		if (all != null) {
			for (WaitingListEntry e : all) {
				if (e != null) queue.enqueue(e);
			}
		}
		initialized = true;
	}

	public List<WaitingListEntry> findAll() {
		ensureInitialized();
		return queue.toList();
	}

	public WaitingListEntry findById(Integer entryId) {
		ensureInitialized();
		if (entryId == null) return null;
		for (WaitingListEntry e : queue.toList()) {
			if (e != null && entryId.equals(e.getWaitingListID())) return e;
		}
		return null;
	}

	public WaitingListEntry add(Integer bookId, Integer memberId) {
		ensureInitialized();
		Book book = bookJpaRepository.findById(bookId).orElse(null);
		Member member = memberJpaRepository.findById(memberId).orElse(null);
		if (book == null || member == null) return null;
		WaitingListEntry entry = new WaitingListEntry();
		entry.setBook(book);
		entry.setMember(member);
		WaitingListEntry saved = jpaRepository.save(entry);
		queue.enqueue(saved);
		return saved;
	}

	public void deleteById(Integer id) {
		ensureInitialized();
		jpaRepository.deleteById(id);
		queue.removeById(id);
	}

	public List<WaitingListEntry> findByBookId(Integer bookId) {
		ensureInitialized();
		return queue.findByBookId(bookId);
	}

	public List<WaitingListEntry> findByMemberId(Integer memberId) {
		ensureInitialized();
		return jpaRepository.findByMember_MemberId(memberId);
	}

	public WaitingListEntry findNextForBook(Integer bookId) {
		return jpaRepository.findFirstByBook_BookIDOrderByWaitingListIDAsc(bookId);
	}
}
