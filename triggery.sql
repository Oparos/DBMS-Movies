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



--albo jego zrobic w funkcji to samo do nazwiska


CREATE OR REPLACE FUNCTION check_login_availability()
RETURNS TRIGGER AS $$
DECLARE
    taken_login varchar[];
BEGIN
    SELECT ARRAY_AGG(login)
    INTO taken_login
    FROM Uzytkownicy

    IF NEW.login = ANY(taken_login) THEN
        RAISE EXCEPTION 'Taki login juz istnieje';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_login_availability_trigger
BEFORE INSERT ON rezerwacje
FOR EACH ROW
EXECUTE FUNCTION check_login_availability();

-- TRIGGER sprawdzajacy czy jest dostpeny taki login


CREATE OR REPLACE FUNCTION update_avg_rating()
RETURNS TRIGGER AS $$
DECLARE
    avg_rating NUMERIC;
BEGIN
    -- Oblicz średnią ocen
    SELECT COALESCE(AVG(ocena_recenzji), 0) INTO avg_rating
    FROM recenzje
    WHERE movie_id = NEW.movie_id;

    -- Aktualizuj wartość pola ocena w tabeli filmów
    UPDATE filmy
    SET ocena = avg_rating
    WHERE movie_id = NEW.movie_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_avg_rating_trigger
AFTER INSERT OR UPDATE ON recenzje
FOR EACH ROW
EXECUTE FUNCTION update_avg_rating();



--tirgger aktuaolizujacy ocene w filmie na podstawie recenzji

CREATE OR REPLACE FUNCTION check_positive_rating()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.ocena <= 0.0 THEN
        RAISE EXCEPTION 'Ocena musi być większa niż 0.0';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_positive_rating_trigger
BEFORE INSERT OR UPDATE ON recenzje
FOR EACH ROW
EXECUTE FUNCTION check_positive_rating();

-- trigger spradzaijacy czy ocena jest wieksza do 0.0

CREATE OR REPLACE FUNCTION check_available_tickets(id INTEGER)
RETURNS TRIGGER AS $$
DECLARE
    available_tickets INTEGER;
BEGIN
    SELECT liczba_dospenych_biletow
    INTO available_tickets
    FROM seanse
    WHERE seans_id = NEW.seans_id;

    IF available_tickets < 0 THEN
        RAISE EXCEPTION 'Nie ma wystarczającej liczby dostępnych biletów';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_available_tickets_trigger
BEFORE INSERT ON bilety
FOR EACH ROW
EXECUTE FUNCTION check_available_tickets();

-- trrigger sprawdzajacy czy bilety sa jeszcze dostepne

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
    SELECT ARRAY_AGG(nr_miejsca)
    INTO taken_seats
    FROM bilety
    WHERE seans_id = NEW.seans_id;

    -- Sprawdź, czy podany numer miejsca mieści się w zakresie i czy nie jest zajęty
    IF NEW.nr_miejsca < seat_range.numer_poczatkowy_miejsca OR NEW.nr_miejsca > seat_range.numer_koncowy_miejsca OR NEW.nr_miejsca = ANY(taken_seats) THEN
        RAISE EXCEPTION 'Nieprawidłowy numer miejsca';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_seat_availability_trigger
BEFORE INSERT ON bilety
FOR EACH ROW
EXECUTE FUNCTION check_seat_availability();

-- TRIGGER sprawdzajacy czy podano opdopwiednie miejsce

CREATE OR REPLACE FUNCTION check_movie_availability()
RETURNS TRIGGER AS $$
DECLARE
    conflicting_seans RECORD;
BEGIN
    -- Sprawdź, czy istnieje kolidujący seans
    SELECT *
    INTO conflicting_seans
    FROM Seanse
    WHERE film_id = NEW.film_id
    AND (
        (NEW.data_poczatek BETWEEN data_poczatek AND data_koniec)
        OR (NEW.data_koniec BETWEEN data_poczatek AND data_koniec)
        OR (NEW.data_poczatek < data_poczatek AND NEW.data_koniec > data_koniec)
    );

    -- Jeśli istnieje kolidujący seans, rzuć wyjątek
    IF FOUND THEN
        RAISE EXCEPTION 'Film koliduje z istniejącym seansem (seans_id: %)', conflicting_seans.seans_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_movie_availability_trigger
BEFORE INSERT ON Seanse
FOR EACH ROW
EXECUTE FUNCTION check_movie_availability();

-- trigger sprawdzajacy czy film jest w odpowiednij luce czasowej





