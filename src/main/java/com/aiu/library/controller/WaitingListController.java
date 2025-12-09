package com.aiu.library.controller;

import com.aiu.library.model.WaitingListEntry;
import com.aiu.library.service.WaitingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiting-list")
public class WaitingListController {

	private final WaitingListService waitingListService;

	public WaitingListController(WaitingListService waitingListService) {
		this.waitingListService = waitingListService;
	}

	@GetMapping
	public ResponseEntity<List<WaitingListEntry>> list() {
		List<WaitingListEntry> list = waitingListService.getAll();
		return ResponseEntity.ok(list);
	}

	@PostMapping("/add/{bookId}")
	public ResponseEntity<?> add(@PathVariable Long bookId, @RequestParam Integer memberId) {
		if (memberId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("memberId is required as request parameter");
		}
		WaitingListEntry entry = waitingListService.add(bookId, memberId);
		if (entry == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not add entry: invalid bookId or memberId");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(entry);
	}

	@DeleteMapping("/remove/{entryId}")
	public ResponseEntity<?> remove(@PathVariable Long entryId) {
		boolean ok = waitingListService.remove(entryId);
		if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
		return ResponseEntity.noContent().build();
	}
}
