package com.example.library_management.controller;

import com.example.library_management.entity.Book;
import com.example.library_management.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lib/books")
@RequiredArgsConstructor
@CrossOrigin()
@Slf4j
public class BookController {
    private final BookService bookService;
    @GetMapping("/allbooks")
    public List<Book> userPage() {
        return bookService.allBooks();
    }
    @GetMapping("/search/book/{isbn}")
    public Book userPage(@PathVariable("isbn") String isbn) {
        return bookService.getBookbyIsbn(isbn);
    }
}
