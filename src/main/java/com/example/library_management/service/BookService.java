package com.example.library_management.service;

import com.example.library_management.DTO.BooksDto;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.User;
import com.example.library_management.repository.BooksRepository;
import com.example.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BooksRepository booksRepository;
    private final UserRepository userRepository;

    public void addBook(BooksDto request) {
        Optional<Book> exists = booksRepository.findByIsbn(request.getIsbn());
        if (exists.isPresent()) {
            Integer quantity = exists.get().getQuantity();
            exists.get().setQuantity(quantity++);
        }else {
            Book newBook = Book.builder()
                    .isbn(request.getIsbn())
                    .quantity(request.getQuantity())
                    .build();
            booksRepository.save(newBook);
        }
    }

    public Book getBookbyId(Long id) {
        return booksRepository.findById(id).orElseThrow(()-> new RuntimeException("Book doesn't exist in the inventory"));
    }
    public Book getBookbyIsbn(String isbn) {
        return booksRepository.findByIsbn(isbn).orElseThrow(()-> new RuntimeException("Book doesn't exist in the inventory"));
    }
    public void removeBook(Long bookId) {
        Book booktoRemove = getBookbyId(bookId);
        booksRepository.delete(booktoRemove);
    }






}
