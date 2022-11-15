package com.example.demo.controllers;

import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import com.example.demo.models.User;
import com.example.demo.repo.BookRepositoryJPA;
import com.example.demo.repo.GenreRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.services.BookService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BookController {

    @Autowired //анотация для создания переменной, ссылающейся на репозиторий
    private BookService bookService; //указание репозитория, к которому обращаемся и название пееременной
    //private PostRepository postRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepositoryJPA bookRepositoryJPA;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/books/{genreID}")
    public String activity(Model model,@PathVariable Long genreID) {
        model.addAttribute("books", bookRepositoryJPA.findAllByGenre(genreRepository.getOne(genreID)));
        model.addAttribute("title", "Книги");
        return "book";
    }

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if(!userService.registration(user)) {
            model.addAttribute("errorMessage", "Пользователь с таким логином уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/book/add") //GetMapping - пользователь переходит по определённому адресу
    public String bookAdd(Model model) {
        model.addAttribute("genres",genreRepository.findAll());
        return "book-add";
    }

    @PostMapping("/book/add") //получение данных из формы
    public String bookBookAdd(@AuthenticationPrincipal User user, @RequestParam String img, @RequestParam String title, @RequestParam String fullText, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, Model model,@RequestParam String genre,@RequestParam String author,@RequestParam int price,@RequestParam String ISBN) { //@RequestParam - получение значений из формы. title - получение значений из данного поля
        Genre genre1 = genreRepository.findByName(genre);
        Book book = new Book(img, title, fullText, date, author, genre1,price,ISBN); //объект на основе модели Post с названием post. (title, fullText) - передача параметров
        bookService.save(book); //сохранение объекта и добавление в бд -> обращение к репозиторию -> обращение к функции save и передача в него объекта, который необходимо сохранить => добавление в таблицу post навых статей, полученных от пользователя
        //postRepository.save(post);
        return "redirect:/genres"; //переадресация пользователя на указанную страницу после добавления статьи
    }

   @GetMapping("/book/{id}") //{id} - динамическое значение url-адреса
   public String bookDetails(@PathVariable(value = "id") long id, Model model,@AuthenticationPrincipal User user) { //@PathVariable - анотация, принимающая динамический параметр из url-адреса (в определённый параметр (long id) помещается значение, полученное из url-адреса
        Optional<Book> book= bookService.findById(id); //нахождение записи по id и помещение в объект book на основе класса Optional и модели <Post>
        if(book.isPresent()) {
            ArrayList<Book> res = new ArrayList<>();
            book.ifPresent(res::add); //из класса Optional переводим в класс ArrayList
            model.addAttribute("book", res);
            Book book1 = bookRepositoryJPA.getOne(id);
            User user1 = userRepository.getOne(user.getId());
            boolean alreadyInBasket = user1.getBasket().contains(book1);
            model.addAttribute("isInBasket",alreadyInBasket);
            return "book-details";
        } else {
            return "redirect:/genres"; //перенаправление на указанную страницу
        }
    }

    @GetMapping("/book/{id}/edit") //редактирование статьи
    public String bookEdit(@PathVariable(value = "id") long id, Model model) { //@PathVariable - анотация, принимающая динамический параметр из url-адреса (в определённый параметр (long id) помещается значение, полученное из url-адреса
        if(!bookService.existsById(id)){ //try - если определённая запись по определённому id не была найдена. иначе false
            return "redirect:/book/{id}"; //перенаправление на указанную страницу
        }
        //Optional<Post> book= postRepository.findById(id);
        Optional<Book> book= bookService.findById(id); //нахождение записи по id и помещение в объект book на основе класса Optional и модели <Post>
        ArrayList<Book> res = new ArrayList<>();
        book.ifPresent(res::add); //из класса Optional переводим в класс ArrayList
        model.addAttribute("book", res);
        return "book-edit";
    }

    @PostMapping("/book/{id}/edit") //получение данных из формы
    public String bookBookUpdate(@PathVariable(value = "id") long id, @RequestParam String img, @RequestParam String title, @RequestParam String fullText, Model model,@RequestParam int price,@RequestParam String ISBN,@RequestParam String author) { //@RequestParam - получение значений из формы. title - получение значений из данного поля
        Book book = bookService.findById(id).orElseThrow(
                () -> new RuntimeException()
        ); //orElseTrow() - исключительная ситуация в случае не нахождения записи
        book.setImg(img);
        book.setTitle(title); //установка введеного заголовка
        book.setFullText(fullText);
        book.setPrice(price);
        book.setISBN(ISBN);
        book.setAuthor(author);
        bookService.save(book); //сохранение обновлённого объекта
        return "redirect:/book/{id}"; //переадресация пользователя на указанную страницу после добавления статьи
    }

    @PostMapping("/book/{id}/remove") //получение данных из формы
    public String bookBookDelete(@PathVariable(value = "id") long id, Model model) { //@RequestParam - получение значений из формы. title - получение значений из данного поля
        Book book = bookService.findById(id).orElseThrow(
                () -> new RuntimeException()
        ); //orElseTrow() - исключительная ситуация в случае не нахождения записи
        bookService.delete(book); //удаление определенной записи
        return "redirect:/books"; //переадресация пользователя на указанную страницу после удаления статьи
    }

    @PostMapping("/book/{id}/addToBasket")
    public String addToBasket(@PathVariable Long id,@AuthenticationPrincipal User user){
        bookService.addToBasket(id,user.getId());
        return "redirect:/book/" + id;
    }

    @GetMapping("/genres")
    public String genres(Model model){
        model.addAttribute("genres",genreRepository.findAll());
        return "genreChose";
    }

    @GetMapping("/basket")
    public String basket(Model model, @AuthenticationPrincipal User user){
        User user1 = userRepository.getOne(user.getId());
        model.addAttribute("userBasket",user1.getBasket());
        return "basket";
    }

    @PostMapping("/book/{id}/removeFromBasket")
    public String removeFromBasket(@PathVariable Long id,@AuthenticationPrincipal User user){
        User user1 = userRepository.getOne(user.getId());
        List<Book> books = user1.getBasket();
        Book book1 = bookRepositoryJPA.getOne(id);
        books.removeIf(book -> book.getTitle().equals(book1.getTitle()));
        user1.setBasket(books);
        userRepository.save(user1);
        return "redirect:/basket";
    }


    @GetMapping("/genre/add")
    public String addGenre(){
        return "addGenre";
    }

    @PostMapping("/addGenre")
    public String addAddGenre(@RequestParam String name){
        Genre genre = new Genre();
        genre.setName(name);
        genreRepository.save(genre);
        return "redirect:/genres";
    }

}