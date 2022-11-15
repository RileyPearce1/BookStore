package com.example.demo.services;

import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.repo.BookRepository;
import com.example.demo.repo.BookRepositoryJPA;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepositoryJPA bookRepositoryJPA;


    public List<Book> findAll() {
        ArrayList<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public Page<Book> findAllByGenre(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size, Sort.by("date")));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findById(long id) {
        return  bookRepository.findById(id);
    }

    public boolean existsById(long id) {
        return bookRepository.existsById(id);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public void addToBasket(Long bookID,Long userId){
        User user = userRepository.getOne(userId);
        List<Book> books = user.getBasket();
        books.add(bookRepositoryJPA.getOne(bookID));
        user.setBasket(books);
        userRepository.save(user);
    }
}
