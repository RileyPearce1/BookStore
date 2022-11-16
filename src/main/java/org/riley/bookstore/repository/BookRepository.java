package org.riley.bookstore.repository;

import org.riley.bookstore.model.Book;
import org.riley.bookstore.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByGenre(Genre genre);
}
