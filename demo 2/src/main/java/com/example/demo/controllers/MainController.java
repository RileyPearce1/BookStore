package com.example.demo.controllers;

import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//@RestController
@Controller
public class MainController { //класс, отвещающий за обработку всех переходов по сайту (@Controller)

    @Autowired private BookService bookService; //указание репозитория, к которому обращаемся и название пееременной

}
