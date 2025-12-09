package com.aiu.library.repository;

import com.aiu.library.datastructures.WaitingQueue;
import com.aiu.library.model.WaitingListEntry;
import com.aiu.library.model.Book;
import com.aiu.library.model.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WaitingListRepository {

	private final WaitingListJpaRepository jpaRepository;
	private final BookJpaRepository bookJpaRepository;
	private final MemberJpaRepository memberJpaRepository;

	// in-memory FIFO queue mirroring DB state after initialization
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
			all = jpaRepository.findAllByOrderByIdAsc();
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

	public WaitingListEntry findById(Long id) {
		ensureInitialized();
		if (id == null) return null;
		for (WaitingListEntry e : queue.toList()) {
			if (e != null && id.equals(e.getId())) return e;
		}
		return null;
	}

	public WaitingListEntry add(Long bookId, Integer memberId) {
		ensureInitialized();
		Book book = bookJpaRepository.findById(bookId.intValue()).orElse(null);
		Member member = memberJpaRepository.findById(memberId).orElse(null);
		if (book == null || member == null) return null;
		WaitingListEntry entry = new WaitingListEntry();
		entry.setBook(book);
		entry.setMember(member);
		WaitingListEntry saved = jpaRepository.save(entry);
		queue.enqueue(saved);
		return saved;
	}

	public void deleteById(Long id) {
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
		// in-memory removal/lookup
		return jpaRepository.findByMember_MemberId(memberId);
	}

	public WaitingListEntry findNextForBook(Integer bookId) {
		// prefer DB query for accurate next entry in concurrent scenarios
		return jpaRepository.findFirstByBook_BookIDOrderByIdAsc(bookId);
	}
}
