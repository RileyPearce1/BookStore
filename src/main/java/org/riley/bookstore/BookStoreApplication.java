package org.riley.bookstore;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(BookStoreApplication.class)
                .run(args);

    }

}
