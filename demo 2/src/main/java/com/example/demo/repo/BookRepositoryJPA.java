package com.example.demo.repo;

import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepositoryJPA extends JpaRepository<Book, Long> {
    List<Book> findAllByGenre(Genre genre);
}
