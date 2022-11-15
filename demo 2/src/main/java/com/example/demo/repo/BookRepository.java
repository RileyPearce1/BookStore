package com.example.demo.repo;

import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {  //extends - наследование. <> - указание модели, с которой работаем и типа данных
}
