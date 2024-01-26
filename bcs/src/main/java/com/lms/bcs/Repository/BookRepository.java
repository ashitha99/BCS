package com.lms.bcs.Repository;

import com.lms.bcs.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> search(String keyword);
}
