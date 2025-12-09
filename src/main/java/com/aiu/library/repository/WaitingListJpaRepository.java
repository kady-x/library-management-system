package com.aiu.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aiu.library.model.WaitingListEntry;
import java.util.List;

@Repository
public interface WaitingListJpaRepository extends JpaRepository<WaitingListEntry, Integer> {

	// Return all waiting list entries ordered by waitingListID (FIFO order)
	List<WaitingListEntry> findAllByOrderByWaitingListIDAsc();

	// Entries for a specific book ordered by insertion (waitingListID) ascending
	List<WaitingListEntry> findByBook_BookIDOrderByWaitingListIDAsc(Integer bookId);

	// Entries for a specific member
	List<WaitingListEntry> findByMember_MemberId(Integer memberId);

	// Get the next waiting entry for a book (oldest)
	WaitingListEntry findFirstByBook_BookIDOrderByWaitingListIDAsc(Integer bookId);

}
