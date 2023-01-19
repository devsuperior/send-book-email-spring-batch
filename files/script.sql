CREATE TABLE tb_user (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	email VARCHAR(60) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE tb_book (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	description TEXT NOT NULL,
	author VARCHAR(60) NOT NULL,
	category VARCHAR(20) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE tb_user_book_loan (
	user_id INT NOT NULL,
	book_id INT NOT NULL,
	loan_date DATE NOT NULL,
	PRIMARY KEY(user_id, book_id),
	FOREIGN KEY (user_id) REFERENCES tb_user(id),
	FOREIGN KEY (book_id) REFERENCES tb_book(id)
);

INSERT INTO tb_user(id, name, email) VALUES (1, 'Maria', 'd7209e49-0f63-4dde-b9a1-9d95da52fa27@mailslurp.com');
INSERT INTO tb_user(id, name, email) VALUES (2, 'João', 'c1bc0ed0-cc60-4835-ad42-85e89bb02225@mailslurp.com');
INSERT INTO tb_user(id, name, email) VALUES (3, 'Ana', '7564e94a-da3f-4e86-969e-8f81775cde76@mailslurp.com');

INSERT INTO tb_book (id, name, description, author, category) VALUES(1, 'Capitães de Areia', 'Lorem ipsum dolor sit amet. Est dicta voluptate sed pariatur laboriosam repellendus!', 'Jorge Amado', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(2, 'Dom Casmurro', 'Lorem ipsum dolor sit amet. Et praesentium nobis ut quaerat voluptate eum volup.', 'Machado de Assis', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(3, 'Quincas Borba', 'Eos doloribus impedit ut dolor sunt sit nostrum libero', 'Machado de Assis', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(4, 'Alguma poesia', 'Lorem ipsum dolor sit amet. Quo voluptates soluta sit.', 'Carlos Drummond de Andrade', 'Poesia');
INSERT INTO tb_book (id, name, description, author, category) VALUES(5, 'A hora da estrela', 'Et sunt quaerat vel provident dolores quo Quis', 'Clarisse Lispector', 'Poesia');
INSERT INTO tb_book (id, name, description, author, category) VALUES(6, 'Tudo sobre o amor', 'Lorem ipsum dolor sit amet. Ut corrupti ullam aut', 'Bell Hooks', 'Humanidade');
INSERT INTO tb_book (id, name, description, author, category) VALUES(7, 'Torto Arado', 'Id tempore quas et aperiam minima ut dolores', 'Itamar Vieira Junior', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(8, 'Os Miseráveis', 'Lorem ipsum dolor sit amet. Aut voluptates', 'Victor Hugo', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(9, 'Dom Quixote', 'Hic nobis dolor ut praesentium aspernatur', 'Miguel de Cervantes', 'Romance');

INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(1, 9, '2023-01-13');
INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(1, 4, '2023-01-13');
INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(2, 6, '2023-01-13');
INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(3, 2, '2023-01-13');