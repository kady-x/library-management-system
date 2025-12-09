package com.aiu.library.controller;

import com.aiu.library.model.WaitingListEntry;
import com.aiu.library.service.WaitingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiting-list")
public class WaitingListController {

	private static final Logger logger = LoggerFactory.getLogger(WaitingListController.class);
	private final WaitingListService waitingListService;

	public WaitingListController(WaitingListService waitingListService) {
		this.waitingListService = waitingListService;
	}

	@GetMapping
	public ResponseEntity<?> list() {
		try {
			logger.info("GET /waiting-list - Fetching all entries");
			List<WaitingListEntry> list = waitingListService.getAll();
			logger.info("Successfully retrieved {} waiting list entries", list.size());
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			logger.error("Error fetching waiting list entries", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to retrieve waiting list entries: " + e.getMessage());
		}
	}

	@PostMapping("/add/{bookId}")
	public ResponseEntity<?> add(@PathVariable Long bookId,
								 @RequestParam(required = true) Integer memberId) {
		try {
			logger.info("POST /waiting-list/add/{} - Adding entry for memberId: {}", bookId, memberId);
			WaitingListEntry entry = waitingListService.add(bookId, memberId);
			logger.info("Successfully added waiting list entry with ID: {}", entry.getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(entry);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid request parameters for adding waiting list entry: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid input: " + e.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("Business logic error while adding waiting list entry: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Operation failed: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error while adding waiting list entry", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to add waiting list entry: " + e.getMessage());
		}
	}

	@DeleteMapping("/remove/{entryId}")
	public ResponseEntity<?> remove(@PathVariable Long entryId) {
		try {
			logger.info("DELETE /waiting-list/remove/{} - Removing entry", entryId);
			boolean success = waitingListService.remove(entryId);
			if (success) {
				logger.info("Successfully removed waiting list entry with ID: {}", entryId);
				return ResponseEntity.noContent().build();
			} else {
				logger.warn("Waiting list entry with ID {} not found", entryId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Waiting list entry not found");
			}
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid entryId parameter: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid input: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error while removing waiting list entry", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to remove waiting list entry: " + e.getMessage());
		}
	}
}
