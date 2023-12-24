CREATE OR REPLACE FUNCTION check_imie_nazwisko()
RETURNS TRIGGER AS $$
BEGIN
    -- Sprawdź, czy imie zawiera tylko litery
    IF NEW.imie ~ '[^a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]' OR NEW.nazwisko ~ '[^a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]' THEN
        RAISE EXCEPTION 'Imie i nazwisko może zawierać tylko litery';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_imie_nazwisko_trigger
BEFORE INSERT ON Uzytkownicy 
FOR EACH ROW
EXECUTE FUNCTION check_imie_nazwisko();

drop trigger check_imie_nazwisko_trigger on uzytkownicy;

CREATE OR REPLACE FUNCTION check_login_availability()
RETURNS TRIGGER AS $$
DECLARE
    taken_login varchar[];
BEGIN
    SELECT ARRAY_AGG(login)
    INTO taken_login
    FROM Uzytkownicy;

    IF NEW.login = ANY(taken_login) THEN
        RAISE EXCEPTION 'Taki login juz istnieje';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_login_availability_trigger
BEFORE INSERT ON Uzytkownicy
FOR EACH ROW
EXECUTE FUNCTION check_login_availability();

CREATE OR REPLACE FUNCTION check_positive_rating()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.ocena_recenzji < 0.0 and NEW.ocena_recenzji > 5.0 THEN
        RAISE EXCEPTION 'Ocena musi być z zakresu 0.0-5.0 ';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_positive_rating_trigger
BEFORE INSERT OR UPDATE ON recenzje
FOR EACH ROW
EXECUTE FUNCTION check_positive_rating();

CREATE OR REPLACE FUNCTION update_avg_rating()
RETURNS TRIGGER AS $$
DECLARE
    avg_rating NUMERIC;
BEGIN
    -- Oblicz średnią ocen
    SELECT COALESCE(AVG(ocena_recenzji), 0) INTO avg_rating
    FROM recenzje
    WHERE film_id = NEW.film_id;

    -- Aktualizuj wartość pola ocena w tabeli filmów
    UPDATE filmy
    SET ocena = avg_rating
    WHERE film_id = NEW.film_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_avg_rating_trigger
AFTER INSERT OR UPDATE ON recenzje
FOR EACH ROW
EXECUTE FUNCTION update_avg_rating();


CREATE OR REPLACE FUNCTION check_seat_availability()
RETURNS TRIGGER AS $$
DECLARE
    taken_seats INTEGER[];
    seat_range RECORD;
BEGIN
    -- Pobierz zakres miejsc z seansu
    SELECT numer_poczatkowy_miejsca, numer_koncowy_miejsca
    INTO seat_range
    FROM Seanse
    WHERE seans_id = NEW.seans_id;

    -- Pobierz zajęte miejsca na dany seans
    SELECT ARRAY_AGG(numer_miejsca)
    INTO taken_seats
    FROM bilety
    WHERE seans_id = NEW.seans_id;

    -- Sprawdź, czy podany numer miejsca mieści się w zakresie i czy nie jest zajęty
    IF NEW.numer_miejsca < seat_range.numer_poczatkowy_miejsca OR NEW.numer_miejsca > seat_range.numer_koncowy_miejsca OR NEW.numer_miejsca = ANY(taken_seats) THEN
        RAISE EXCEPTION 'Nieprawidłowy numer miejsca';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_seat_availability_trigger
BEFORE INSERT ON bilety
FOR EACH ROW
EXECUTE FUNCTION check_seat_availability();

insert into uzytkownicy (login,haslo,imie,nazwisko,recenzent) values ('admin','admin','ąć','test',true); 
insert into uzytkownicy (login,haslo,imie,nazwisko,recenzent) values ('admin1','admin1','ąć','test',true); 
insert into uzytkownicy (login,haslo,imie,nazwisko,recenzent) values ('admin2','admin2','ąćaaa','aaatest',true); 
insert  into  filmy (tytul,rezyser,rok_produkcji,ocena,producent) values ('test','test',2000,0.0,'test');
insert  into  filmy (tytul,rezyser,rok_produkcji,ocena,producent) values ('test2','test2',2100,0.0,'test2');

drop trigger check_seat_availability_trigger on bilety;
drop function check_seat_availability;
select * from uzytkownicy u ;
select * from filmy f ;

ALTER TABLE filmy ALTER COLUMN ocena type decimal(2,1);
ALTER TABLE recenzje  ALTER COLUMN ocena_recenzji type decimal(2,1);


insert into recenzje (film_id,uzytkownik_id,tytul_recenzji,tresc_recenzji,ocena_recenzji) values (1,3,'test','test',4.5);
insert into seanse (film_id, data_poczatek,data_koniec,numer_poczatkowy_miejsca,numer_koncowy_miejsca,liczba_dostepnych_biletow) 
values (1,'2023-12-30 11:30:30', '2023-12-30 13:30:30',1,5,5);

select *from seanse s ;

insert into bilety (seans_id,uzytkownik_id,numer_miejsca,cena) values (1,3,5,30.2);


