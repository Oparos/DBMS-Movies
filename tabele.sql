CREATE TABLE Filmy (
    film_id SERIAL PRIMARY KEY,
    tytul VARCHAR(255),
    rezyser VARCHAR(255),
    rok_produkcji INT,
    ocena DECIMAL(2,1),
    producent VARCHAR(255) -- Możesz dostosować długość VARCHAR, aby pasowała do rzeczywistej długości producenta
);



CREATE TABLE Aktorzy (
    aktor_id SERIAL PRIMARY KEY,
    imie VARCHAR(255),
    nazwisko VARCHAR(255)
);

CREATE TABLE FilmAktor (
    id SERIAL PRIMARY KEY,
    film_id INT REFERENCES Filmy(film_id),
    id_aktora INT REFERENCES Aktorzy(aktor_id), -- Zmiana na id_aktora
    rola VARCHAR(255)
);
CREATE TABLE Kategorie (
    kategoria_id SERIAL PRIMARY KEY,
    nazwa_kategorii VARCHAR(255)
);

CREATE TABLE FilmyKategorie (
    film_id INT REFERENCES Filmy(film_id),
    kategoria_id INT REFERENCES Kategorie(kategoria_id),
    PRIMARY KEY(film_id, kategoria_id)
);

CREATE TABLE Uzytkownicy (
    uzytkownik_id SERIAL PRIMARY KEY,
    login VARCHAR(255),
    haslo VARCHAR(255),
    imie VARCHAR(255),
    nazwisko VARCHAR(255),
    recenzent BOOLEAN
);

CREATE TABLE Recenzje (
    recenzja_id SERIAL PRIMARY KEY,
    film_id INT REFERENCES Filmy(film_id),
    uzytkownik_id INT REFERENCES Uzytkownicy(uzytkownik_id),
    tytul_recenzji VARCHAR(255),
    tresc_recenzji TEXT,
    ocena_recenzji DECIMAL(2,1)
);

CREATE TABLE Seanse (
    seans_id SERIAL PRIMARY KEY,
    film_id INT REFERENCES Filmy(film_id),
    data_poczatek TIMESTAMP,
    data_koniec TIMESTAMP,
    numer_poczatkowy_miejsca INT,
    numer_koncowy_miejsca INT,
    liczba_dostepnych_biletow INT
);

CREATE TABLE Bilety (
    bilet_id SERIAL PRIMARY KEY,
    seans_id INT REFERENCES Seanse(seans_id),
    uzytkownik_id INT REFERENCES Uzytkownicy(uzytkownik_id),
    numer_miejsca INT,
    cena DECIMAL(5,2)
);

