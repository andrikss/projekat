package ac.rs.ftn.webProjekat.Controller;

import ac.rs.ftn.webProjekat.Dto.ZahtjevZaAktivacijuNalogaAutoraDto;
import ac.rs.ftn.webProjekat.Entity.*;
import ac.rs.ftn.webProjekat.Service.AutorService;
import ac.rs.ftn.webProjekat.Service.KorisnikService;
import ac.rs.ftn.webProjekat.Service.MailService;
import ac.rs.ftn.webProjekat.Service.ZahtjevZaAktivacijuNalogaAutoraService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/zahtjevi")
public class ZahtjevRestController {

    @Autowired
    private ZahtjevZaAktivacijuNalogaAutoraService zahtjevZaAktivacijuNalogaAutoraService;

    @Autowired
    private MailService mailService;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private AutorService autorService;


    //private final String adresa = "andriks5667@outlook.com";

    //private final String lozinka = "torbica123";
    @GetMapping("/lista")
    public ResponseEntity<List<ZahtjevZaAktivacijuNalogaAutoraDto>> listaZahtjeva(HttpSession httpSession)
    {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        List<ZahtjevZaAktivacijuNalogaAutora> zahtjevi = zahtjevZaAktivacijuNalogaAutoraService.findAll();
        List<ZahtjevZaAktivacijuNalogaAutoraDto> ret = new ArrayList<>();

        for (ZahtjevZaAktivacijuNalogaAutora it : zahtjevi) {
            ZahtjevZaAktivacijuNalogaAutoraDto dt = new ZahtjevZaAktivacijuNalogaAutoraDto(it);
            ret.add(dt);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    //jedan zahtjev
    @GetMapping("/{zahtjevId}")
    public ResponseEntity<?> getZahtjevById(@PathVariable Long zahtjevId, HttpSession httpSession) {
        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");

        if (loggedUser == null) {
            return new ResponseEntity<>("Nema sesije!", HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator!", HttpStatus.FORBIDDEN);
        }

        ZahtjevZaAktivacijuNalogaAutora zahtjev = zahtjevZaAktivacijuNalogaAutoraService.findById(zahtjevId);

        if (zahtjev == null) {
            return new ResponseEntity<>("Zahtjev nije pronađen!", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(zahtjev, HttpStatus.OK);
    }

   /* private void sendEmailToAddress(String toAddress, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.live.com"); // Smtp host za Outlook
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(adresa, lozinka);
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adresa));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email successfully sent!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }*/

    //dodavanje zahtjeva
    @PostMapping("/dodajZahtjev")
    public ResponseEntity<String> addZahtjev(@RequestBody ZahtjevZaAktivacijuNalogaAutoraDto zahtjevZaAktivacijuNalogaAutoraDto)
    {

        if (!zahtjevZaAktivacijuNalogaAutoraDto.isValid()) {
            return new ResponseEntity<>("Nisu ispravno uneseni podaci!", HttpStatus.BAD_REQUEST);
        }

        String autorEmail = zahtjevZaAktivacijuNalogaAutoraDto.getEmailAdresa();
        String autorIme = zahtjevZaAktivacijuNalogaAutoraService.generisiKorisnickoIme(autorEmail);
        Autor existingAutor = (Autor) zahtjevZaAktivacijuNalogaAutoraService.findKorisnikByEmail(autorEmail);

        if (existingAutor == null) {
            // Ako ne postoji, kreiramo novog autora
            Autor newAutor = new Autor(autorEmail, autorIme);
            zahtjevZaAktivacijuNalogaAutoraService.saveAutor(newAutor);
            existingAutor = newAutor;
        }


        // Sada imamo postojeceg autora (existingAutor)
       /* if (!existingAutor.getUlogaKorisnika().equals(UlogaKorisnika.AUTOR.toString())) {
            return new ResponseEntity<>("Ne pripada autoru email adresa!", HttpStatus.BAD_REQUEST);
        }*/

        ZahtjevZaAktivacijuNalogaAutora noviZahtjev = new ZahtjevZaAktivacijuNalogaAutora(zahtjevZaAktivacijuNalogaAutoraDto);
        noviZahtjev.setAutor(existingAutor);

        zahtjevZaAktivacijuNalogaAutoraService.saveZahtjev(noviZahtjev);

        return new ResponseEntity<>("Novi zahtjev je dodan!", HttpStatus.OK);
    }

    //odobri zahtjev
    @PutMapping("/prihvatiZahtjev/{id}")
    public ResponseEntity<String> allowZahtjev(@PathVariable(name="id") Long zahtevId,
                                                                   HttpSession httpSession)
    {

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }
        //loggedUser = korisnikService.findById(loggedUser.getId());

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator!", HttpStatus.FORBIDDEN);
        }

        ZahtjevZaAktivacijuNalogaAutora targetZahtev = zahtjevZaAktivacijuNalogaAutoraService.findById(zahtevId);
        if (targetZahtev == null) {
            return new ResponseEntity<>("Ne postoji zahtjev!", HttpStatus.BAD_REQUEST);
        }
        if (targetZahtev.getStatus() != StatusZahtjeva.NA_CEKANJU) {
            return new ResponseEntity<>("Zahtjev nije na cekanju!", HttpStatus.FORBIDDEN);
        }


        targetZahtev.setStatus(StatusZahtjeva.ODOBREN);
        zahtjevZaAktivacijuNalogaAutoraService.saveZahtjev(targetZahtev);


        Autor targetAutor = (Autor) korisnikService.findByEmail(targetZahtev.getEmailAdresa());
        if (targetAutor == null) {
            return new ResponseEntity<>("Ne mogu da pronadjem autora sa tom email adresom!", HttpStatus.BAD_REQUEST);
        }



       /* byte[] passwordArray = new byte[10];
        new Random().nextBytes(passwordArray);
        String generatedPassword = new String(passwordArray, Charset.forName("UTF-8"));
        */
        String generatedPassword = generateRandomPassword(9);
        System.out.println("LOZINKA JE" + generatedPassword);
        targetAutor.setLozinka(generatedPassword);
        targetAutor.setAktivan(true);
        zahtjevZaAktivacijuNalogaAutoraService.saveKorisnik(targetAutor);
        String poruka = "Vaš nalog je uspješno aktiviran. Vaša lozinka je: " + generatedPassword;
       // System.out.println(targetZahtev.getStatus().toString());
       // List<ZahtjevZaAktivacijuNalogaAutora> lista = zahtjevZaAktivacijuNalogaAutoraService.findAll();
        mailService.sendSimpleMessage(targetAutor.getEmailAdresa(), "Aktivacija naloga", poruka);

        //slanje imejla boze pomozi mi
       /* String emailText =
                "Vas zahtjev za nalog autora as adresom \'"+
                        targetZahtev.getAutor().getEmailAdresa()+"\' je prihvacen!\n"+
                        "lozinka je: \'"+generatedPassword+"\'";
        String emailSubject = "Zahtjev za aktivaciju autor naloga";
        String toAddress = targetZahtev.getEmailAdresa();*/

       // sendEmailToAddress(toAddress,emailSubject,emailText);

        return new ResponseEntity<>("Zahtjev je odobren!!", HttpStatus.OK);
    }

    public String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] passwordBytes = new byte[length];
        random.nextBytes(passwordBytes);
        String generatedPassword = Base64.getEncoder().encodeToString(passwordBytes);

        return generatedPassword;
    }
    @PutMapping("/odbijZahtjev/{id}")
    public ResponseEntity<String> denyZahtjev(@PathVariable(name="id") Long zahtevId,
                                                                  HttpSession httpSession)
    {

        Korisnik loggedUser = (Korisnik) httpSession.getAttribute("loggedUser");
        Autor targetAutor = autorService.findById(loggedUser.getId());
        if (loggedUser == null) {
            return new ResponseEntity<>("No session!", HttpStatus.FORBIDDEN);
        }

        if (!loggedUser.getUlogaKorisnika().equals(UlogaKorisnika.ADMINISTRATOR.toString())) {
            return new ResponseEntity<>("Niste administrator!", HttpStatus.FORBIDDEN);
        }

        ZahtjevZaAktivacijuNalogaAutora targetZahtev = zahtjevZaAktivacijuNalogaAutoraService.findById(zahtevId);
        if (targetZahtev == null) {
            return new ResponseEntity<>("Ne mogu da pronadjem zahtjev!", HttpStatus.BAD_REQUEST);
        }
        if (targetZahtev.getStatus() != StatusZahtjeva.NA_CEKANJU) {
            return new ResponseEntity<>("Status zahtjev nije na cekanju!", HttpStatus.FORBIDDEN);
        }

        targetZahtev.setStatus(StatusZahtjeva.ODBIJEN);
        zahtjevZaAktivacijuNalogaAutoraService.saveZahtjev(targetZahtev);
        System.out.println(targetZahtev.getStatus().toString());

        /*String toAddress = targetZahtev.getEmailAdresa();
        String emailSubject = "Zahtjev za aktivaciju autor naloga";
        String emailText = "Vas zahtjev za nalog autora sa adresom \'"+
                targetZahtev.getAutor().getEmailAdresa()+"\' je odbijen!";
        */
        //sendEmailToAddress(toAddress,emailSubject,emailText);
        String poruka = "Vaš zahtjev za aktivaciju naloga je odbijen.";
        mailService.sendSimpleMessage(targetZahtev.getEmailAdresa(), "Odbijanje zahtjeva", poruka);
        return new ResponseEntity<>("Zahtjev je odbijen!", HttpStatus.OK);
    }
}
