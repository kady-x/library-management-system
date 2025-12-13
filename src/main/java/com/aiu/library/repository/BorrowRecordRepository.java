package com.aiu.library.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aiu.library.model.BorrowRecord;

@Repository
public class BorrowRecordRepository {

	private final BorrowRecordJpaRepository jpaRepository;

	public BorrowRecordRepository(BorrowRecordJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	public BorrowRecord insert(BorrowRecord record) {
		return jpaRepository.save(record);
	}

	public BorrowRecord save(BorrowRecord record) {
		return jpaRepository.save(record);
	}

	public BorrowRecord findById(Integer id) {
		return jpaRepository.findById(id).orElse(null);
	}

	public List<BorrowRecord> findAll() {
		return jpaRepository.findAll();
	}

	public List<BorrowRecord> findActiveByBookId(Integer bookId) {
		return jpaRepository.findActiveByBookId(bookId);
	}

	public void deleteById(Integer id) {
		jpaRepository.deleteById(id);
	}

	public List<BorrowRecord> findOverdueBooks() {
		return jpaRepository.findOverdueBooks();
	}

	public List<BorrowRecord> findOverdueBooks(java.time.LocalDate startDate, java.time.LocalDate endDate) {
		return jpaRepository.findOverdueBooks(startDate, endDate);
	}

	public List<Object[]> findMostBorrowedBooks() {
		return jpaRepository.findMostBorrowedBooks();
	}

	public List<Object[]> findMostBorrowedBooks(java.time.LocalDate startDate, java.time.LocalDate endDate) {
		return jpaRepository.findMostBorrowedBooks(startDate, endDate);
	}

	public List<Object[]> findMemberActivity() {
		return jpaRepository.findMemberActivity();
	}

	public List<Object[]> findMemberActivity(java.time.LocalDate startDate, java.time.LocalDate endDate) {
		return jpaRepository.findMemberActivity(startDate, endDate);
	}

	public List<BorrowRecord> findByMemberId(Integer memberId) {
		return jpaRepository.findByMemberId(memberId);
	}

	public List<BorrowRecord> findByMemberIdAndReturnStatus(Integer memberId, Boolean returnStatus) {
		return jpaRepository.findByMemberIdAndReturnStatus(memberId, returnStatus);
	}
}
