package com.aiu.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiu.library.model.BorrowRecord;
import com.aiu.library.service.BorrowService;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private static final Logger logger = LoggerFactory.getLogger(BorrowController.class);

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRecords() {
        try {
            logger.info("GET /api/borrow - Fetching all borrow records");
            List<BorrowRecord> records = borrowService.getAllRecords();
            logger.info("Successfully retrieved {} borrow records", records.size());
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("Error fetching all borrow records", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve borrow records: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecordById(@PathVariable Integer id) {
        try {
            logger.info("GET /api/borrow/{} - Fetching borrow record", id);
            BorrowRecord record = borrowService.getRecordById(id);
            if (record != null) {
                logger.info("Successfully retrieved borrow record with ID: {}", id);
                return ResponseEntity.ok(record);
            } else {
                logger.warn("Borrow record with ID {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Borrow record not found");
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid borrow record ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching borrow record with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve borrow record: " + e.getMessage());
        }
    }

    @PostMapping("/issue")
    public ResponseEntity<?> issueBook(@RequestBody IssueBookRequest request) {
        try {
            logger.info("POST /api/borrow/issue - Issuing book ID: {} to member ID: {}", 
                       request.getBookId(), request.getMemberId());
            
            if (request.getMemberId() == null) {
                logger.warn("Missing required field: memberId");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid input: Member ID is required");
            }
            if (request.getBookId() == null) {
                logger.warn("Missing required field: bookId");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid input: Book ID is required");
            }
            
            BorrowRecord record = borrowService.issueBook(request.getMemberId(), request.getBookId(), request.getLoanDays());
            logger.info("Successfully issued book");
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request parameters for issuing book: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while issuing book", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to issue book: " + e.getMessage());
        }
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<?> returnBook(@PathVariable Integer borrowId) {
        try {
            logger.info("POST /api/borrow/return/{} - Processing book return", borrowId);
            BorrowRecord record = borrowService.returnBook(borrowId);
            if (record != null) {
                logger.info("Successfully processed return for borrow record ID: {}", borrowId);
                return ResponseEntity.ok(record);
            } else {
                logger.warn("Borrow record with ID {} not found for return", borrowId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Borrow record not found");
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid borrow ID for return: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while processing return for borrow ID: {}", borrowId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process return: " + e.getMessage());
        }
    }

    @PostMapping("/renew/{id}")
    public ResponseEntity<?> renewBook(@PathVariable Integer id, @RequestParam(required = false) Integer days) {
        try {
            logger.info("POST /api/borrow/renew/{} - Renewing for {} days", id, days != null ? days : "default");
            BorrowRecord record = borrowService.renewBook(id, days);
            if (record != null) {
                logger.info("Successfully renewed borrow record ID: {}", id);
                return ResponseEntity.ok(record);
            } else {
                logger.warn("Borrow record with ID {} not found for renewal", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Borrow record not found");
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid parameters for renewal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while renewing borrow record ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to renew book: " + e.getMessage());
        }
    }

    @GetMapping("/records")
    public ResponseEntity<?> getRecords() {
        try {
            logger.info("GET /api/borrow/records - Fetching all borrow records");
            List<BorrowRecord> records = borrowService.getAllRecords();
            logger.info("Successfully retrieved {} borrow records", records.size());
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("Error fetching borrow records", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve borrow records: " + e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getRecordsByMemberId(@PathVariable Integer memberId) {
        try {
            logger.info("GET /api/borrow/member/{} - Fetching borrow records for member", memberId);
            List<BorrowRecord> records = borrowService.getRecordsByMemberId(memberId);
            logger.info("Successfully retrieved {} borrow records for member {}", records.size(), memberId);
            return ResponseEntity.ok(records);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid member ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching records for member ID: {}", memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve member borrow records: " + e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<?> getActiveRecordsByMemberId(@PathVariable Integer memberId) {
        try {
            logger.info("GET /api/borrow/member/{}/active - Fetching active borrow records for member", memberId);
            List<BorrowRecord> records = borrowService.getActiveRecordsByMemberId(memberId);
            logger.info("Successfully retrieved {} active borrow records for member {}", records.size(), memberId);
            return ResponseEntity.ok(records);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid member ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching active records for member ID: {}", memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve member active borrow records: " + e.getMessage());
        }
    }
    
    public static class IssueBookRequest {
        private Integer memberId;
        private Integer bookId;
        private Integer loanDays;

        public Integer getMemberId() {
            return memberId;
        }

        public void setMemberId(Integer memberId) {
            this.memberId = memberId;
        }

        public Integer getBookId() {
            return bookId;
        }

        public void setBookId(Integer bookId) {
            this.bookId = bookId;
        }

        public Integer getLoanDays() {
            return loanDays;
        }

        public void setLoanDays(Integer loanDays) {
            this.loanDays = loanDays;
        }
    }
}
