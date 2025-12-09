package com.aiu.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.model.WaitingListEntry;
import com.aiu.library.repository.WaitingListRepository;

@Service
public class WaitingListService {

	private static final Logger logger = LoggerFactory.getLogger(WaitingListService.class);
	private final WaitingListRepository repository;

	public WaitingListService(WaitingListRepository repository) {
		this.repository = repository;
	}

	public List<WaitingListEntry> getAll() {
		try {
			logger.info("Fetching all waiting list entries");
			List<WaitingListEntry> entries = repository.findAll();
			logger.info("Retrieved {} waiting list entries", entries.size());
			return entries;
		} catch (Exception e) {
			logger.error("Error fetching all waiting list entries", e);
			throw new RuntimeException("Failed to retrieve waiting list entries", e);
		}
	}

	@Transactional
	public WaitingListEntry add(Long bookId, Integer memberId) {
		logger.info("Adding waiting list entry for bookId: {}, memberId: {}", bookId, memberId);

		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Valid bookId is required");
		}
		if (memberId == null || memberId <= 0) {
			throw new IllegalArgumentException("Valid memberId is required");
		}

		try {
			WaitingListEntry entry = repository.add(bookId.intValue(), memberId);
			if (entry == null) {
				logger.warn("Failed to add waiting list entry - invalid bookId or memberId provided");
				throw new IllegalStateException("Could not create waiting list entry - invalid book or member");
			}
			logger.info("Successfully added waiting list entry with ID: {}", entry.getId());
			return entry;
		} catch (IllegalArgumentException | IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error adding waiting list entry for bookId: {}, memberId: {}", bookId, memberId, e);
			throw new RuntimeException("Failed to add waiting list entry", e);
		}
	}

	@Transactional
	public boolean remove(Long entryId) {
		logger.info("Removing waiting list entry with ID: {}", entryId);

		if (entryId == null || entryId <= 0) {
			throw new IllegalArgumentException("Valid entryId is required");
		}

		try {
			WaitingListEntry entry = repository.findById(entryId.intValue());
			if (entry == null) {
				logger.warn("Waiting list entry with ID {} not found", entryId);
				return false;
			}

			repository.deleteById(entryId.intValue());
			logger.info("Successfully removed waiting list entry with ID: {}", entryId);
			return true;
		} catch (Exception e) {
			logger.error("Error removing waiting list entry with ID: {}", entryId, e);
			throw new RuntimeException("Failed to remove waiting list entry", e);
		}
	}
}
