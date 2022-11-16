package org.riley.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.riley.bookstore.service.BookService;
import org.springframework.stereotype.Controller;

//@RestController
@Controller
@RequiredArgsConstructor
public class MainController { //класс, отвещающий за обработку всех переходов по сайту (@Controller)

    private final BookService bookService; //указание репозитория, к которому обращаемся и название пееременной

}
