/* KNJIGA*/
INSERT INTO KNJIGA(naslov, ISBN, datum_objavljivanja, broj_strana, opis, ocjena)
    VALUES ('Sumrak Saga', 'SS234', '2008-07-01', 280, 'Najbolja fantasticna knjiga svih vremena!', 9);
INSERT INTO KNJIGA(naslov, ISBN, datum_objavljivanja, broj_strana, opis, ocjena)
    VALUES ('Septembarska Svetla', 'SS555', '1999-01-09', 190, 'Za tinejdzere, ali i za odrasle, vrlo dobra!', 7);
INSERT INTO KNJIGA(naslov, ISBN, datum_objavljivanja, broj_strana, opis, ocjena)
    VALUES ('Carry On', 'CO908', '2016-05-05', 420, 'YA knjiga', 10);
INSERT INTO KNJIGA(naslov, ISBN, datum_objavljivanja, broj_strana, opis, ocjena)
    VALUES ('Gdje srce spava', 'GSC15', '2022-09-15', 120, 'Poezija mlade autorke<3', 10);

/*ZANR*/
INSERT INTO ZANR(naziv) VALUES ('fantazija');
INSERT INTO ZANR(naziv) VALUES ('horor');
INSERT INTO ZANR(naziv) VALUES ('tinejdz');
INSERT INTO ZANR(naziv) VALUES ('poezija');

/*povezujem KNJIGU I ZANR*/
INSERT INTO KNJIGA_ZANR(knjiga_id, zanr_id) VALUES (1,1);
INSERT INTO KNJIGA_ZANR(knjiga_id, zanr_id) VALUES (2,2);
INSERT INTO KNJIGA_ZANR(knjiga_id, zanr_id) VALUES (3,3);
INSERT INTO KNJIGA_ZANR(knjiga_id, zanr_id) VALUES (4,4);

/* ADMINISTRATOR, CITALAC, AUTOR*/
INSERT INTO KORISNIK(ime, prezime, korisnicko_ime, email_adresa, lozinka, datum_rodjenja, uloga_korisnika)
    VALUES ('Teodor', 'Vasic', 'todex', 't.vasic89@gmail.com', 'g00dread5','2000-01-02', 'Administrator');

INSERT INTO KORISNIK(ime, prezime, korisnicko_ime, email_adresa, lozinka, datum_rodjenja, uloga_korisnika)
    VALUES ('Ivana', 'Lazarevic', 'ivkaa', 'iva.l566@gmail.com', 'ivanajenaj', '2002-10-04', 'Citalac');
INSERT INTO KORISNIK(ime, prezime, korisnicko_ime, email_adresa, lozinka, datum_rodjenja, uloga_korisnika)
    VALUES('Sasa', 'Dormanovic', 'sashaxd', 'dormanovics@gmail.com', 'fudbal123', '2000-03-01', 'Citalac');
INSERT INTO KORISNIK(ime, prezime, korisnicko_ime, email_adresa, lozinka, datum_rodjenja, uloga_korisnika)
    VALUES('Vanja', 'Tekic', 'periodtSis', 'tekicvanja01@gmail.com', 'periodtt', '2001-06-10', 'Citalac');

INSERT INTO KORISNIK(ime, prezime, korisnicko_ime, email_adresa, lozinka, datum_rodjenja, uloga_korisnika, aktivan)
    VALUES('Milica', 'Mrkonjic', 'kovrdzica', 'milica.mrkonjic15@gmail.com', 'aronvorner', '2001-09-10', 'Autor', TRUE);
INSERT INTO KORISNIK(ime, prezime, korisnicko_ime, email_adresa, lozinka, datum_rodjenja, uloga_korisnika, aktivan)
    VALUES('Vuk', 'Stepic', 'vuleX', 'stepicvuk@gmail.com', 'ovojelozinka', '2000-01-01', 'Autor', FALSE);

/* povezujem AUTORA I KNJIGU*/
INSERT INTO AUTOR_KNJIGA(autor_id, knjiga_id) VALUES (5,4);

/*POLICE*/
INSERT INTO POLICA(naziv, tip) VALUES ('WantToRead', 1);
INSERT INTO POLICA(naziv, tip) VALUES ('CurrentlyReading', 2);
INSERT INTO POLICA(naziv, tip) VALUES ('Read', 3);

INSERT INTO POLICA(naziv, tip) VALUES ('Moja Polica Ivana', 0);
INSERT INTO POLICA(naziv, tip) VALUES ('Moja Polica Vanja', 0);
INSERT INTO POLICA(naziv, tip) VALUES ('Moja Polica Sasa', 0);

INSERT INTO POLICA(naziv, tip) VALUES ('WantToRead', 1);
INSERT INTO POLICA(naziv, tip) VALUES ('CurrentlyReading', 2);
INSERT INTO POLICA(naziv, tip) VALUES ('Read', 3);

INSERT INTO POLICA(naziv, tip) VALUES ('WantToRead', 1);
INSERT INTO POLICA(naziv, tip) VALUES ('CurrentlyReading', 2);
INSERT INTO POLICA(naziv, tip) VALUES ('Read', 3);

INSERT INTO POLICA(naziv, tip) VALUES ('WantToRead', 1);
INSERT INTO POLICA(naziv, tip) VALUES ('CurrentlyReading', 2);
INSERT INTO POLICA(naziv, tip) VALUES ('Read', 3);

INSERT INTO POLICA(naziv, tip) VALUES ('WantToRead', 1);
INSERT INTO POLICA(naziv, tip) VALUES ('CurrentlyReading', 2);
INSERT INTO POLICA(naziv, tip) VALUES ('Read', 3);

/*povezujem police sa citaocima/korisnicima, PRVO PRIMARNE*/
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (2,1);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (2,2);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (2,3);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (2,4);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (4,5);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (3,6);

INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (3,7);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (3,8);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (3,9);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (4,10);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (4,11);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (4,12);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (5,13);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (5,14);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (5,15);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (6,16);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (6,17);
INSERT INTO KORISNIK_POLICE(korisnik_id, police_id) VALUES (6,18);


/*STAVKA POLICE*/
INSERT INTO STAVKA_POLICE(stavka_police_knjiga, polica_id) VALUES (1, 1);
INSERT INTO STAVKA_POLICE(stavka_police_knjiga, polica_id) VALUES (2, 5);
INSERT INTO STAVKA_POLICE(stavka_police_knjiga, polica_id) VALUES (3, 9);
INSERT INTO STAVKA_POLICE(stavka_police_knjiga, polica_id) VALUES (4, 1);

/*RECENZIJA*/
INSERT INTO RECENZIJA(ocjena, tekst, korisnik_id, stavka_police_id) VALUES (8,'Odlicna!!', 2, 1);
INSERT INTO RECENZIJA(ocjena, tekst, korisnik_id, stavka_police_id) VALUES (9,'Jako zanimljiva, daje puno savjeta', 4, 2);

/*ZAHTJEV ZA AKTIVACIJU NALOGA*/
INSERT INTO ZAHTJEV_ZA_AKTIVACIJU_NALOGA_AUTORA(email_adresa, telefon, poruka, datum, status, autor_id)
    VALUES ('stepicvuk@gmail.com', '+38765222334', 'Molim Vas da mi odobrite nalog autora', '2022-11-19', 0, 6);








