package com.example.library_management.repository;

import com.example.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    public Optional<Book> findByIsbn(String isbn);

    public Optional<Book> deleteBookById(Long id);

}
