package com.aiu.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aiu.library.model.Book;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT COALESCE(MAX(b.bookID), 0) FROM Book b")
    Integer findMaxBookID();
}
