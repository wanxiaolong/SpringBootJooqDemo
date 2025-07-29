CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    year_released INT NOT NULL,
    title VARCHAR(255) NOT NULL UNIQUE,
    director VARCHAR(255),
    genre VARCHAR(255),
    likes INT DEFAULT 0
);

INSERT INTO movies (year_released, title, director, genre, likes) VALUES
(2023, 'The JOOQ Adventure', 'Code Smith', 'Adventure', 100),
(2022, 'SQL Masterpiece', 'Query Queen', 'Drama', 50);