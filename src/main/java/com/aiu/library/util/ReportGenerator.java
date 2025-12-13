package com.aiu.library.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiu.library.model.BorrowRecord;

public class ReportGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    public static List<BorrowRecord> mergeSortBorrowRecords(List<BorrowRecord> records) {
        if (records == null) {
            logger.warn("Cannot sort null list of BorrowRecords");
            throw new IllegalArgumentException("Records list cannot be null");
        }

        if (records.isEmpty()) {
            logger.debug("Empty list provided for merge sort, returning empty list");
            return new ArrayList<>();
        }

        logger.debug("Starting Merge Sort for {} BorrowRecords by due date ascending", records.size());
        long startTime = System.currentTimeMillis();

        List<BorrowRecord> sortedRecords = new ArrayList<>(records);
        mergeSort(sortedRecords, 0, sortedRecords.size() - 1,
            Comparator.comparing(BorrowRecord::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));

        long duration = System.currentTimeMillis() - startTime;
        logger.debug("Merge Sort completed for BorrowRecords in {} ms", duration);

        return sortedRecords;
    }

    public static List<BorrowRecord> quickSortBorrowRecords(List<BorrowRecord> records) {
        if (records == null) {
            logger.warn("Cannot sort null list of BorrowRecords");
            throw new IllegalArgumentException("Records list cannot be null");
        }

        if (records.isEmpty()) {
            logger.debug("Empty list provided for quick sort, returning empty list");
            return new ArrayList<>();
        }

        logger.debug("Starting Quick Sort for {} BorrowRecords by due date ascending", records.size());
        long startTime = System.currentTimeMillis();

        List<BorrowRecord> sortedRecords = new ArrayList<>(records);
        quickSort(sortedRecords, 0, sortedRecords.size() - 1,
            Comparator.comparing(BorrowRecord::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));

        long duration = System.currentTimeMillis() - startTime;
        logger.debug("Quick Sort completed for BorrowRecords in {} ms", duration);

        return sortedRecords;
    }

    public static List<Map<String, Object>> mergeSortMostBorrowed(List<Map<String, Object>> books) {
        if (books == null) {
            logger.warn("Cannot sort null list of books");
            throw new IllegalArgumentException("Books list cannot be null");
        }

        if (books.isEmpty()) {
            logger.debug("Empty list provided for merge sort, returning empty list");
            return new ArrayList<>();
        }

        logger.debug("Starting Merge Sort for {} books by borrow count descending", books.size());
        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> sortedBooks = new ArrayList<>(books);
        mergeSort(sortedBooks, 0, sortedBooks.size() - 1,
            Comparator.comparing((Map<String, Object> book) -> (Long) book.get("borrowCount"),
                Comparator.reverseOrder()));

        long duration = System.currentTimeMillis() - startTime;
        logger.debug("Merge Sort completed for books in {} ms", duration);

        return sortedBooks;
    }

    public static List<Map<String, Object>> quickSortMostBorrowed(List<Map<String, Object>> books) {
        if (books == null) {
            logger.warn("Cannot sort null list of books");
            throw new IllegalArgumentException("Books list cannot be null");
        }

        if (books.isEmpty()) {
            logger.debug("Empty list provided for quick sort, returning empty list");
            return new ArrayList<>();
        }

        logger.debug("Starting Quick Sort for {} books by borrow count descending", books.size());
        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> sortedBooks = new ArrayList<>(books);
        quickSort(sortedBooks, 0, sortedBooks.size() - 1,
            Comparator.comparing((Map<String, Object> book) -> (Long) book.get("borrowCount"),
                Comparator.reverseOrder()));

        long duration = System.currentTimeMillis() - startTime;
        logger.debug("Quick Sort completed for books in {} ms", duration);

        return sortedBooks;
    }

    public static List<Map<String, Object>> mergeSortMemberActivity(List<Map<String, Object>> members) {
        if (members == null) {
            logger.warn("Cannot sort null list of members");
            throw new IllegalArgumentException("Members list cannot be null");
        }

        if (members.isEmpty()) {
            logger.debug("Empty list provided for merge sort, returning empty list");
            return new ArrayList<>();
        }

        logger.debug("Starting Merge Sort for {} members by activity count descending", members.size());
        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> sortedMembers = new ArrayList<>(members);
        mergeSort(sortedMembers, 0, sortedMembers.size() - 1,
            Comparator.comparing((Map<String, Object> member) -> (Long) member.get("activityCount"),
                Comparator.reverseOrder()));

        long duration = System.currentTimeMillis() - startTime;
        logger.debug("Merge Sort completed for members in {} ms", duration);

        return sortedMembers;
    }

    public static List<Map<String, Object>> quickSortMemberActivity(List<Map<String, Object>> members) {
        if (members == null) {
            logger.warn("Cannot sort null list of members");
            throw new IllegalArgumentException("Members list cannot be null");
        }

        if (members.isEmpty()) {
            logger.debug("Empty list provided for quick sort, returning empty list");
            return new ArrayList<>();
        }

        logger.debug("Starting Quick Sort for {} members by activity count descending", members.size());
        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> sortedMembers = new ArrayList<>(members);
        quickSort(sortedMembers, 0, sortedMembers.size() - 1,
            Comparator.comparing((Map<String, Object> member) -> (Long) member.get("activityCount"),
                Comparator.reverseOrder()));

        long duration = System.currentTimeMillis() - startTime;
        logger.debug("Quick Sort completed for members in {} ms", duration);

        return sortedMembers;
    }

    private static <T> void mergeSort(List<T> list, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            mergeSort(list, left, middle, comparator);
            mergeSort(list, middle + 1, right, comparator);

            merge(list, left, middle, right, comparator);
        }
    }

    private static <T> void merge(List<T> list, int left, int middle, int right, Comparator<T> comparator) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        List<T> leftList = new ArrayList<>(n1);
        List<T> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) {
            leftList.add(list.get(left + i));
        }
        for (int j = 0; j < n2; ++j) {
            rightList.add(list.get(middle + 1 + j));
        }

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (comparator.compare(leftList.get(i), rightList.get(j)) <= 0) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);

            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
