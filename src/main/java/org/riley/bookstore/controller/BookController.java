package org.riley.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.riley.bookstore.model.Book;
import org.riley.bookstore.model.Genre;
import org.riley.bookstore.model.User;
import org.riley.bookstore.repository.BookRepository;
import org.riley.bookstore.repository.GenreRepository;
import org.riley.bookstore.repository.UserRepository;
import org.riley.bookstore.service.BookService;
import org.riley.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class BookController {

    //анотация для создания переменной, ссылающейся на репозиторий
    private final BookService bookService; //указание репозитория, к которому обращаемся и название пееременной
    //private PostRepository postRepository;
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;


    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/books/{genreID}")
    public String activity(Model model, @PathVariable Long genreID) {
        model.addAttribute("books", bookRepository.findAllByGenre(genreRepository.getOne(genreID)));
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
        if (!userService.registration(user)) {
            model.addAttribute("errorMessage", "Пользователь с таким логином уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/book/add") //GetMapping - пользователь переходит по определённому адресу
    public String bookAdd(Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        return "book-add";
    }

    @PostMapping("/book/add") //получение данных из формы
    public String bookBookAdd(@AuthenticationPrincipal User user, @RequestParam String img, @RequestParam String title,
                              @RequestParam String fullText,
                              @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                              Model model, @RequestParam String genre, @RequestParam String author,
                              @RequestParam int price, @RequestParam String ISBN) {
        //@RequestParam - получение значений из формы. title - получение значений из данного поля
        Genre genreFromRepository = genreRepository.findByName(genre);
        Book book = new Book()
                .setImg(img)
                .setTitle(title)
                .setFullText(fullText)
                .setDate(date)
                .setAuthor(author)
                .setGenre(genreFromRepository)
                .setPrice(price)
                .setISBN(ISBN);

        bookService.save(book); //сохранение объекта и добавление в бд -> обращение к репозиторию -> обращение к функции save и передача в него объекта, который необходимо сохранить => добавление в таблицу post навых статей, полученных от пользователя
        return "redirect:/genres"; //переадресация пользователя на указанную страницу после добавления статьи
    }

    @GetMapping("/book/{id}") //{id} - динамическое значение url-адреса
    public String bookDetails(@PathVariable(value = "id") long id, Model model, @AuthenticationPrincipal User user) { //@PathVariable - анотация, принимающая динамический параметр из url-адреса (в определённый параметр (long id) помещается значение, полученное из url-адреса
        Optional<Book> book = bookService.findById(id); //нахождение записи по id и помещение в объект book на основе класса Optional и модели <Post>
        if (book.isPresent()) {
            ArrayList<Book> res = new ArrayList<>();
            book.ifPresent(res::add); //из класса Optional переводим в класс ArrayList
            model.addAttribute("book", res);
            Book book1 = bookRepository.getReferenceById(id);
            User user1 = userRepository.getReferenceById(user.getId());
            boolean alreadyInBasket = user1.getBasket().contains(book1);
            model.addAttribute("isInBasket", alreadyInBasket);
            return "book-details";
        } else {
            return "redirect:/genres"; //перенаправление на указанную страницу
        }
    }

    @GetMapping("/book/{id}/edit") //редактирование статьи
    public String bookEdit(@PathVariable(value = "id") long id, Model model) { //@PathVariable - анотация, принимающая динамический параметр из url-адреса (в определённый параметр (long id) помещается значение, полученное из url-адреса
        if (!bookService.existsById(id)) { //try - если определённая запись по определённому id не была найдена. иначе false
            return "redirect:/book/{id}"; //перенаправление на указанную страницу
        }
        //Optional<Post> book= postRepository.findById(id);
        Optional<Book> book = bookService.findById(id); //нахождение записи по id и помещение в объект book на основе класса Optional и модели <Post>
        ArrayList<Book> res = new ArrayList<>();
        book.ifPresent(res::add); //из класса Optional переводим в класс ArrayList
        model.addAttribute("book", res);
        return "book-edit";
    }

    @PostMapping("/book/{id}/edit") //получение данных из формы
    public String bookBookUpdate(@PathVariable(value = "id") long id, @RequestParam String img, @RequestParam String title, @RequestParam String fullText, Model model, @RequestParam int price, @RequestParam String ISBN, @RequestParam String author) { //@RequestParam - получение значений из формы. title - получение значений из данного поля
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
    public String addToBasket(@PathVariable Long id, @AuthenticationPrincipal User user) {
        bookService.addToBasket(id, user.getId());
        return "redirect:/book/" + id;
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        return "genreChose";
    }

    @GetMapping("/basket")
    public String basket(Model model, @AuthenticationPrincipal User currentUser) {
        User user = userRepository.getReferenceById(currentUser.getId());
        model.addAttribute("userBasket", user.getBasket());
        return "basket";
    }

    @PostMapping("/book/{id}/removeFromBasket")
    public String removeFromBasket(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        User user = userRepository.getReferenceById(currentUser.getId());
        List<Book> books = user.getBasket();
        Book book = bookRepository.getReferenceById(id);
        books.removeIf(b -> b.getTitle().equals(book.getTitle()));
        user.setBasket(books);
        userRepository.save(user);
        return "redirect:/basket";
    }


    @GetMapping("/genre/add")
    public String addGenre() {
        return "addGenre";
    }

    @PostMapping("/addGenre")
    public String addAddGenre(@RequestParam String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genreRepository.save(genre);
        return "redirect:/genres";
    }

}