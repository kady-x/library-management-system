package com.aiu.library.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aiu.library.model.BorrowRecord;
import com.aiu.library.repository.BorrowRecordRepository;
import com.aiu.library.util.ReportGenerator;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final BorrowRecordRepository borrowRecordRepository;

    public ReportService(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<BorrowRecord> getOverdueBooks() {
        return getOverdueBooks(50);
    }

    public List<BorrowRecord> getOverdueBooks(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }

        logger.debug("Fetching overdue books with limit {}", limit);
        try {
            // Fetch all overdue records first
            List<BorrowRecord> overdue = borrowRecordRepository.findOverdueBooks();
            
            // Apply sorting using Merge Sort by due date ascending
            logger.debug("Sorting {} overdue books by due date ascending using Merge Sort", overdue.size());
            List<BorrowRecord> sortedOverdue = ReportGenerator.mergeSortBorrowRecords(overdue);
            
            // Apply limit after sorting
            List<BorrowRecord> result = sortedOverdue.stream().limit(limit).collect(Collectors.toList());
            
            logger.debug("Successfully sorted and limited to {} overdue books", result.size());
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to retrieve and sort overdue books", e);
            throw new RuntimeException("Failed to retrieve and sort overdue books", e);
        }
    }

    public List<BorrowRecord> getOverdueBooks(LocalDate startDate, LocalDate endDate) {
        return getOverdueBooks(startDate, endDate, 50);
    }

    public List<BorrowRecord> getOverdueBooks(LocalDate startDate, LocalDate endDate, int limit) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }

        logger.debug("Fetching overdue books between {} and {} with limit {}", startDate, endDate, limit);
        try {
            // Fetch all overdue records first
            List<BorrowRecord> overdue = borrowRecordRepository.findOverdueBooks(startDate, endDate);
            
            // Apply sorting using Merge Sort by due date ascending
            logger.debug("Sorting {} overdue books by due date ascending using Merge Sort", overdue.size());
            List<BorrowRecord> sortedOverdue = ReportGenerator.mergeSortBorrowRecords(overdue);
            
            // Apply limit after sorting
            List<BorrowRecord> result = sortedOverdue.stream().limit(limit).collect(Collectors.toList());
            
            logger.debug("Successfully sorted and limited to {} overdue books", result.size());
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to retrieve and sort overdue books", e);
            throw new RuntimeException("Failed to retrieve and sort overdue books", e);
        }
    }

    public List<Map<String, Object>> getMostBorrowedBooks() {
        return getMostBorrowedBooks(50);
    }

    public List<Map<String, Object>> getMostBorrowedBooks(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }

        logger.debug("Fetching most borrowed books with limit {}", limit);
        try {
            List<Object[]> results = borrowRecordRepository.findMostBorrowedBooks();
            List<Map<String, Object>> books = results.stream()
                .map(row -> {
                    com.aiu.library.model.Book book = (com.aiu.library.model.Book) row[0];
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", book.getTitle());
                    map.put("author", book.getAuthor());
                    map.put("borrowCount", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
                
            // Apply sorting using Quick Sort by borrow count descending
            logger.debug("Sorting {} books by borrow count descending using Quick Sort", books.size());
            List<Map<String, Object>> sortedBooks = ReportGenerator.quickSortMostBorrowed(books);
            
            // Apply limit after sorting
            List<Map<String, Object>> result = sortedBooks.stream().limit(limit).collect(Collectors.toList());
            
            logger.debug("Successfully sorted and limited to {} most borrowed books", result.size());
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to retrieve and sort most borrowed books", e);
            throw new RuntimeException("Failed to retrieve and sort most borrowed books", e);
        }
    }

    public List<Map<String, Object>> getMostBorrowedBooks(LocalDate startDate, LocalDate endDate) {
        return getMostBorrowedBooks(startDate, endDate, 50);
    }

    public List<Map<String, Object>> getMostBorrowedBooks(LocalDate startDate, LocalDate endDate, int limit) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }

        logger.debug("Fetching most borrowed books between {} and {} with limit {}", startDate, endDate, limit);
        try {
            List<Object[]> results = borrowRecordRepository.findMostBorrowedBooks(startDate, endDate);
            List<Map<String, Object>> books = results.stream()
                .map(row -> {
                    com.aiu.library.model.Book book = (com.aiu.library.model.Book) row[0];
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", book.getTitle());
                    map.put("author", book.getAuthor());
                    map.put("borrowCount", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
                
            // Apply sorting using Quick Sort by borrow count descending
            logger.debug("Sorting {} books by borrow count descending using Quick Sort", books.size());
            List<Map<String, Object>> sortedBooks = ReportGenerator.quickSortMostBorrowed(books);
            
            // Apply limit after sorting
            List<Map<String, Object>> result = sortedBooks.stream().limit(limit).collect(Collectors.toList());
            
            logger.debug("Successfully sorted and limited to {} most borrowed books", result.size());
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to retrieve and sort most borrowed books", e);
            throw new RuntimeException("Failed to retrieve and sort most borrowed books", e);
        }
    }

    public List<Map<String, Object>> getMemberActivity() {
        return getMemberActivity(50);
    }

    public List<Map<String, Object>> getMemberActivity(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }

        logger.debug("Fetching member activity with limit {}", limit);
        try {
            List<Object[]> results = borrowRecordRepository.findMemberActivity();
            List<Map<String, Object>> members = results.stream()
                .map(row -> {
                    com.aiu.library.model.Member member = (com.aiu.library.model.Member) row[0];
                    Map<String, Object> map = new HashMap<>();
                    map.put("memberName", member.getName());
                    map.put("memberEmail", member.getContactInfo());
                    map.put("activityCount", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
                
            // Apply sorting using Merge Sort by activity count descending
            logger.debug("Sorting {} members by activity count descending using Merge Sort", members.size());
            List<Map<String, Object>> sortedMembers = ReportGenerator.mergeSortMemberActivity(members);
            
            // Apply limit after sorting
            List<Map<String, Object>> result = sortedMembers.stream().limit(limit).collect(Collectors.toList());
            
            logger.debug("Successfully sorted and limited to {} member activities", result.size());
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to retrieve and sort member activity", e);
            throw new RuntimeException("Failed to retrieve and sort member activity", e);
        }
    }

    public List<Map<String, Object>> getMemberActivity(LocalDate startDate, LocalDate endDate) {
        return getMemberActivity(startDate, endDate, 50);
    }

    public List<Map<String, Object>> getMemberActivity(LocalDate startDate, LocalDate endDate, int limit) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }

        logger.debug("Fetching member activity between {} and {} with limit {}", startDate, endDate, limit);
        try {
            List<Object[]> results = borrowRecordRepository.findMemberActivity(startDate, endDate);
            List<Map<String, Object>> members = results.stream()
                .map(row -> {
                    com.aiu.library.model.Member member = (com.aiu.library.model.Member) row[0];
                    Map<String, Object> map = new HashMap<>();
                    map.put("memberName", member.getName());
                    map.put("memberEmail", member.getContactInfo());
                    map.put("activityCount", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
                
            // Apply sorting using Merge Sort by activity count descending
            logger.debug("Sorting {} members by activity count descending using Merge Sort", members.size());
            List<Map<String, Object>> sortedMembers = ReportGenerator.mergeSortMemberActivity(members);
            
            // Apply limit after sorting
            List<Map<String, Object>> result = sortedMembers.stream().limit(limit).collect(Collectors.toList());
            
            logger.debug("Successfully sorted and limited to {} member activities", result.size());
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to retrieve and sort member activity", e);
            throw new RuntimeException("Failed to retrieve and sort member activity", e);
        }
    }
}
