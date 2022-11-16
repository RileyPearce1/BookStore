package org.riley.bookstore.service;

import org.riley.bookstore.model.Book;
import org.riley.bookstore.model.User;
import org.riley.bookstore.repository.BookPagingRepository;
import org.riley.bookstore.repository.BookRepository;
import org.riley.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired private BookPagingRepository bookPagingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;


    public List<Book> findAll() {
        ArrayList<Book> books = new ArrayList<>();
        bookPagingRepository.findAll().forEach(books::add);
        return books;
    }

    public Page<Book> findAllByGenre(int page, int size) {
        return bookPagingRepository.findAll(PageRequest.of(page, size, Sort.by("date")));
    }

    public Book save(Book book) {
        return bookPagingRepository.save(book);
    }

    public Optional<Book> findById(long id) {
        return  bookPagingRepository.findById(id);
    }

    public boolean existsById(long id) {
        return bookPagingRepository.existsById(id);
    }

    public void delete(Book book) {
        bookPagingRepository.delete(book);
    }

    public void addToBasket(Long bookID,Long userId){
        User user = userRepository.getOne(userId);
        List<Book> books = user.getBasket();
        books.add(bookRepository.getOne(bookID));
        user.setBasket(books);
        userRepository.save(user);
    }
}
