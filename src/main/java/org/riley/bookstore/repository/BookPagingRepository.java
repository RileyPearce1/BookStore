package org.riley.bookstore.repository;

import org.riley.bookstore.model.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPagingRepository extends PagingAndSortingRepository<Book, Long> {  //extends - наследование. <> - указание модели, с которой работаем и типа данных
}
