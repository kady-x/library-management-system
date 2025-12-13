package com.aiu.library.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aiu.library.model.BorrowRecord;

@Repository
public interface BorrowRecordJpaRepository extends JpaRepository<BorrowRecord, Integer> {
    @Query("SELECT br FROM BorrowRecord br WHERE br.book.bookID = :bookId AND br.returnStatus = false")
    List<BorrowRecord> findActiveByBookId(@Param("bookId") Integer bookId);

    @Query("SELECT br FROM BorrowRecord br WHERE br.returnStatus = false AND br.dueDate < CURRENT_DATE")
    List<BorrowRecord> findOverdueBooks();

    @Query("SELECT br FROM BorrowRecord br WHERE br.returnStatus = false AND br.dueDate < CURRENT_DATE AND br.borrowDate >= :startDate AND br.borrowDate <= :endDate")
    List<BorrowRecord> findOverdueBooks(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT br.book, COUNT(br) as borrowCount FROM BorrowRecord br WHERE br.borrowDate >= :startDate AND br.borrowDate <= :endDate GROUP BY br.book ORDER BY borrowCount DESC")
    List<Object[]> findMostBorrowedBooks(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT br.book, COUNT(br) as borrowCount FROM BorrowRecord br GROUP BY br.book ORDER BY borrowCount DESC")
    List<Object[]> findMostBorrowedBooks();

    @Query("SELECT br.member, COUNT(br) as activityCount FROM BorrowRecord br WHERE br.borrowDate >= :startDate AND br.borrowDate <= :endDate GROUP BY br.member ORDER BY activityCount DESC")
    List<Object[]> findMemberActivity(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT br.member, COUNT(br) as activityCount FROM BorrowRecord br GROUP BY br.member ORDER BY activityCount DESC")
    List<Object[]> findMemberActivity();

    @Query("SELECT br FROM BorrowRecord br WHERE br.member.memberId = :memberId ORDER BY br.borrowDate DESC")
    List<BorrowRecord> findByMemberId(@Param("memberId") Integer memberId);

    @Query("SELECT br FROM BorrowRecord br WHERE br.member.memberId = :memberId AND br.returnStatus = :returnStatus ORDER BY br.borrowDate DESC")
    List<BorrowRecord> findByMemberIdAndReturnStatus(@Param("memberId") Integer memberId, @Param("returnStatus") Boolean returnStatus);
}
