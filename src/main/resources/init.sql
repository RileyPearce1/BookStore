INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'USER');

INSERT INTO genre (id, name) VALUES (1, 'Программирование');

INSERT INTO book (isbn, author, date, full_text, genre_id, img, price, title, id) values ('9785041959326','Бауэр К., Кинг Г., Грегори Г. ', '01/01/2017 08:00:00.000', '<p>Самая полная книга о Hibernate! Одновременно и учебник, и руководство. Данная книга описывает разработку приложения с использованием Hibernate, связывая воедино сотни отдельных примеров. Hibernate – наиболее популярный инструмент Java для работы с базами данных, предоставляющим автоматическое прозрачное объектно-реляционное отображение, что значительно упрощает работу с SQL-базами данных в приложениях Java.<br></p>', 1, 'https://cdn1.ozone.ru/multimedia/1024317756.jpg', 2000, 'Java Persistence API и Hibernate', 1);