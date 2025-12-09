package com.aiu.library.service;

import org.springframework.stereotype.Service;
import com.aiu.library.repository.BorrowRecordRepository;
import com.aiu.library.model.BorrowRecord;
import com.aiu.library.model.Book;
import com.aiu.library.model.Member;
import java.util.List;

@Service
public class BorrowService {

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

	public BorrowRecord issueBook(Integer memberId, Integer bookId, int loanDays) {
		Member member = memberService.getMemberById(memberId);
		if (member == null) throw new RuntimeException("Member not found");

		Book book = bookService.getBookById(bookId);
		if (book == null) throw new RuntimeException("Book not found");

		if (Boolean.FALSE.equals(book.getAvailabilityStatus())) {
			throw new RuntimeException("Book is not available");
		}

		BorrowRecord record = new BorrowRecord();
		record.issueBook(member, book, loanDays <= 0 ? 14 : loanDays);

		// persist record
		BorrowRecord saved = borrowRecordRepository.insert(record);

		// update book status and member borrowed list
		book.setAvailabilityStatus(false);
		bookService.updateBook(book.getBookID(), book);

		member.getBorrowedBooks().add(book);
		memberService.updateMember(member.getMemberId(), member);

		return saved;
	}

	public BorrowRecord returnBook(Integer borrowId) {
		BorrowRecord record = borrowRecordRepository.findById(borrowId);
		if (record == null) throw new RuntimeException("Borrow record not found");
		if (Boolean.TRUE.equals(record.getReturnStatus())) {
			return record; // already returned
		}

		record.returnBook();
		borrowRecordRepository.save(record);

		Book book = record.getBook();
		Member member = record.getMember();

		if (book != null) {
			book.setAvailabilityStatus(true);
			bookService.updateBook(book.getBookID(), book);
		}

		if (member != null && book != null) {
			member.getBorrowedBooks().removeIf(b -> b.getBookID().equals(book.getBookID()));
			memberService.updateMember(member.getMemberId(), member);
		}

		return record;
	}

	public BorrowRecord renewBook(Integer borrowId, int extraDays) {
		BorrowRecord record = borrowRecordRepository.findById(borrowId);
		if (record == null) throw new RuntimeException("Borrow record not found");
		if (Boolean.TRUE.equals(record.getReturnStatus())) throw new RuntimeException("Cannot renew a returned book");

		record.renewBook(extraDays <= 0 ? 7 : extraDays);
		borrowRecordRepository.save(record);
		return record;
	}

	public List<BorrowRecord> getAllRecords() {
		return borrowRecordRepository.findAll();
	}
}
