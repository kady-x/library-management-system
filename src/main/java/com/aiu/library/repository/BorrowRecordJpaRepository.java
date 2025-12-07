package com.aiu.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aiu.library.model.BorrowRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface BorrowRecordJpaRepository extends JpaRepository<BorrowRecord, Integer> {
    @Query("SELECT br FROM BorrowRecord br WHERE br.book.bookID = :bookId AND br.returnStatus = false")
    List<BorrowRecord> findActiveByBookId(@Param("bookId") Integer bookId);
}
