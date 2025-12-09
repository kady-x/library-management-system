package com.aiu.library.service;

import com.aiu.library.model.WaitingListEntry;
import com.aiu.library.repository.WaitingListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingListService {

	private final WaitingListRepository repository;

	public WaitingListService(WaitingListRepository repository) {
		this.repository = repository;
	}

	public List<WaitingListEntry> getAll() {
		return repository.findAll();
	}

	public WaitingListEntry add(Long bookId, Integer memberId) {
		return repository.add(bookId, memberId);
	}

	public boolean remove(Long entryId) {
		WaitingListEntry e = repository.findById(entryId);
		if (e == null) return false;
		repository.deleteById(entryId);
		return true;
	}
}



