package com.aiu.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiu.library.model.WaitingListEntry;

@Repository
public interface WaitingListJpaRepository extends JpaRepository<WaitingListEntry, Long> {

	// Return all waiting list entries ordered by id (FIFO order)
	List<WaitingListEntry> findAllByOrderByIdAsc();

	// Entries for a specific book ordered by insertion (id) ascending
	List<WaitingListEntry> findByBook_BookIDOrderByIdAsc(Integer bookId);

	// Entries for a specific member
	List<WaitingListEntry> findByMember_MemberId(Integer memberId);

	// Get the next waiting entry for a book (oldest)
	WaitingListEntry findFirstByBook_BookIDOrderByIdAsc(Integer bookId);

}
    

