package com.aiu.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.model.Book;
import com.aiu.library.model.BorrowRecord;
import com.aiu.library.model.Member;
import com.aiu.library.repository.BorrowRecordRepository;

@Service
public class BorrowService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowService.class);
    private static final int DEFAULT_LOAN_DAYS = 14;
    private static final int DEFAULT_RENEWAL_DAYS = 7;

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookService bookService;
    private final MemberService memberService;

    public BorrowService(BorrowRecordRepository borrowRecordRepository,
                         BookService bookService,
                         MemberService memberService) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @Transactional
    public BorrowRecord issueBook(Integer memberId, Integer bookId, int loanDays) {
        logger.info("Issuing book with ID: {} to member with ID: {} for {} days", bookId, memberId, loanDays);

        if (memberId == null) {
            throw new IllegalArgumentException("Member ID is required");
        }
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID is required");
        }

        try {
            Member member = memberService.getMemberById(memberId);
            if (member == null) {
                throw new IllegalStateException("Member with ID " + memberId + " not found");
            }

            Book book = bookService.getBookById(bookId);
            if (book == null) {
                throw new IllegalStateException("Book with ID " + bookId + " not found");
            }

            int availableQuantity = book.getQuantity() != null ? book.getQuantity() : 1;
            if (availableQuantity <= 0) {
                throw new IllegalStateException("Book with ID " + bookId + " is not available for borrowing");
            }

            BorrowRecord record = new BorrowRecord();
            int effectiveLoanDays = loanDays <= 0 ? DEFAULT_LOAN_DAYS : loanDays;
            record.issueBook(member, book, effectiveLoanDays);

            BorrowRecord saved = borrowRecordRepository.insert(record);
            logger.debug("Borrow record created with ID: {}", saved.getBorrowID());

            int currentQuantity = book.getQuantity() != null ? book.getQuantity() : 1;
            int newQuantity = currentQuantity - 1;
            book.setQuantity(newQuantity);
            book.setAvailabilityStatus(newQuantity > 0);
            bookService.updateBook(book.getBookID(), book);

            logger.info("Successfully issued book ID: {} to member ID: {}, borrow record ID: {}", 
                       bookId, memberId, saved.getBorrowID());
            return saved;
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Failed to issue book: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to issue book ID: {} to member ID: {}", bookId, memberId, e);
            throw new RuntimeException("Failed to issue book: " + e.getMessage(), e);
        }
    }

    @Transactional
    public BorrowRecord returnBook(Integer borrowId) {
        logger.info("Processing return for borrow record ID: {}", borrowId);

        if (borrowId == null) {
            throw new IllegalArgumentException("Borrow record ID is required");
        }

        try {
            BorrowRecord record = borrowRecordRepository.findById(borrowId);
            if (record == null) {
                throw new IllegalStateException("Borrow record with ID " + borrowId + " not found");
            }

            if (Boolean.TRUE.equals(record.getReturnStatus())) {
                throw new IllegalStateException("Borrow record with ID " + borrowId + " has already been returned");
            }

            record.returnBook();
            borrowRecordRepository.save(record);

            Book book = record.getBook();
            if (book != null) {
                int newQuantity = (book.getQuantity() != null ? book.getQuantity() : 0) + 1;
                book.setQuantity(newQuantity);
                book.setAvailabilityStatus(true);
                bookService.updateBook(book.getBookID(), book);
                logger.debug("Updated book ID: {} quantity to: {}", book.getBookID(), newQuantity);
            }

            logger.info("Successfully processed return for borrow record ID: {}", borrowId);
            return record;
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Failed to process return: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to process return for borrow record ID: {}", borrowId, e);
            throw new RuntimeException("Failed to return book: " + e.getMessage(), e);
        }
    }

    @Transactional
    public BorrowRecord renewBook(Integer borrowId, int extraDays) {
        logger.info("Renewing borrow record ID: {} for {} extra days", borrowId, extraDays);

        if (borrowId == null) {
            throw new IllegalArgumentException("Borrow record ID is required");
        }

        try {
            BorrowRecord record = borrowRecordRepository.findById(borrowId);
            if (record == null) {
                throw new IllegalStateException("Borrow record with ID " + borrowId + " not found");
            }

            if (Boolean.TRUE.equals(record.getReturnStatus())) {
                throw new IllegalStateException("Cannot renew borrow record ID " + borrowId + ": book already returned");
            }

            int effectiveExtraDays = extraDays <= 0 ? DEFAULT_RENEWAL_DAYS : extraDays;
            record.renewBook(effectiveExtraDays);
            borrowRecordRepository.save(record);

            logger.info("Successfully renewed borrow record ID: {} for {} days", borrowId, effectiveExtraDays);
            return record;
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Failed to renew book: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to renew borrow record ID: {}", borrowId, e);
            throw new RuntimeException("Failed to renew book: " + e.getMessage(), e);
        }
    }

    public List<BorrowRecord> getAllRecords() {
        logger.debug("Fetching all borrow records");
        try {
            List<BorrowRecord> records = borrowRecordRepository.findAll();
            logger.debug("Retrieved {} borrow records", records.size());
            return records;
        } catch (Exception e) {
            logger.error("Failed to retrieve borrow records", e);
            throw new RuntimeException("Failed to retrieve borrow records", e);
        }
    }

    public BorrowRecord getRecordById(Integer borrowId) {
        logger.debug("Fetching borrow record with ID: {}", borrowId);
        try {
            if (borrowId == null) {
                throw new IllegalArgumentException("Borrow record ID cannot be null");
            }
            BorrowRecord record = borrowRecordRepository.findById(borrowId);
            if (record != null) {
                logger.debug("Found borrow record with ID: {}", borrowId);
            } else {
                logger.debug("Borrow record with ID {} not found", borrowId);
            }
            return record;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch borrow record with ID: {}", borrowId, e);
            throw new RuntimeException("Failed to fetch borrow record", e);
        }
    }

    public List<BorrowRecord> getRecordsByMemberId(Integer memberId) {
        logger.debug("Fetching borrow records for member ID: {}", memberId);
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            List<BorrowRecord> records = borrowRecordRepository.findByMemberId(memberId);
            logger.debug("Retrieved {} borrow records for member ID: {}", records.size(), memberId);
            return records;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch borrow records for member ID: {}", memberId, e);
            throw new RuntimeException("Failed to fetch borrow records by member", e);
        }
    }

    public List<BorrowRecord> getActiveRecordsByMemberId(Integer memberId) {
        logger.debug("Fetching active borrow records for member ID: {}", memberId);
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            List<BorrowRecord> records = borrowRecordRepository.findByMemberIdAndReturnStatus(memberId, false);
            logger.debug("Retrieved {} active borrow records for member ID: {}", records.size(), memberId);
            return records;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch active borrow records for member ID: {}", memberId, e);
            throw new RuntimeException("Failed to fetch active borrow records by member", e);
        }
    }
}
