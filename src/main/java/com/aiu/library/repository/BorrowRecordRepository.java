package com.aiu.library.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
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
}
