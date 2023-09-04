<template>
  <div class="register">

    <label for="emailAdresa">Email adresa:</label>
    <input v-model="registrujseDTO.emailAdresa" /><br />
    <label for="lozinka">Lozinka:</label>
    <input type="password" v-model="registrujseDTO.lozinka" /><br />
    <label for="ponovljenaLozinka" >Ponovljena lozinka:</label>
    <input type="password" v-model="registrujseDTO.ponovljenaLozinka" /><br />
    <label for="korisnickoIme" >Korisničko ime:</label>
    <input v-model="registrujseDTO.korisnickoIme" /><br />

    <label for="ime" >Ime:</label>
    <input v-model="registrujseDTO.ime" /><br />
    <label for="prezime">Prezime:</label>
    <input v-model="registrujseDTO.prezime" /><br />
    <label for="datumRodjenja" >Datum rođenja:</label>
    <input type="date" v-model="registrujseDTO.datumRodjenja" /><br />


    <button v-on:click="submit()">POTVRDI</button>
  </div>
</template>

<script>
export default {
  name: "Register",
  data: function() {
    return {
      registrujseDTO: {
        emailAdresa: "",
        lozinka: "",
        ponovljenaLozinka: "",
        korisnickoIme: "",
        ime: "",
        prezime: "",
        datumRodjenja: "",
        opis: "",

      },
    };
  },
  methods: {
    getValueOfField(fieldName, objToSearchIn) {

      let start_poz = objToSearchIn.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = objToSearchIn.substring(start_poz, objToSearchIn.length);
      //console.log("rest_of_obj="+rest_of_obj);
      const end_of_value_poz = rest_of_obj.indexOf("\"");
      const value = rest_of_obj.substring(0, end_of_value_poz);
      //console.log("value="+value);

      return value;
    },

    submit: function () {
        console.log(JSON.stringify(this.registrujseDTO))
        fetch("http://localhost:9090/api/korisnici/register", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-type": "application/json",
          },
          body: JSON.stringify(this.registrujseDTO),
        })

      .
        then((res) => {
          //console.log("success:"+res);
          if (res.ok) {
            console.log('Successfully created new citalac!');
            return res.json();
          } else if (res.status === 400) {
            alert('Bad request!');
          } else if (res.status === 403) {
            alert('Forbidden!');
          } else if (res.status === 404) {
            alert('Not found!');
          } else {
            console.log('Failed to create new citalac!');
            throw new Error('Login failed');
          }
        })
            .then((data) => {
              //console.log("Success : " + data);
              localStorage.setItem('loggedUser', JSON.stringify(data));
              window.location.reload();
            })
            .catch((err) => {
              console.log("Error : " + err);
              alert(err);
            })
            .then(() => {
              const loggedUserObj = localStorage.getItem('loggedUser');
              console.log('Logged User Object:', loggedUserObj); // Dodajte ovu liniju
              const ulogaKorisnika = this.getValueOfField('ulogaKorisnika', loggedUserObj);
              console.log('ulogaKorisnika=' + ulogaKorisnika);


              if (ulogaKorisnika === 'Citalac') {
                this.$router.push('/homeCitalac');
              } else if (ulogaKorisnika === 'Autor') {
                this.$router.push('/homeAutor');
              } else if (ulogaKorisnika === 'Administrator') {
                this.$router.push('/homeAdministrator');
              } else {
                this.$router.push('/');
              }
            });


    },

  },

}
</script>

<style scoped>
/* Stilizacija za formu za registraciju */
.register {
  text-align: center;
}

.register h2 {
  font-size: 24px; /* Velika veličina naslova */
  margin-bottom: 20px; /* Razmak ispod naslova */
}

.register label {
  display: block; /* Polje za unos će biti prikazano kao blok element, jedno ispod drugog */
  margin-bottom: 10px; /* Razmak između labela i input polja */
  font-weight: bold;
  color: white;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.2); /* Dodajte sjenku tekstu */

}

.register input {
  width: 100%;
  padding: 0px;
  margin-bottom: 10px; /* Dodajte razmak između input polja */
  border: 2px solid #ccc; /* Dodajte okvir za input polje */
  border-radius: 5px; /* Zaobljeni rubovi za input polje */
  font-size: 14px; /* Velika veličina fonta za unos */
}

.register button {
  background-color: #3a9550; /* Boja dugmeta za submit */
  color: white; /* Boja teksta na dugmetu */
  padding: 10px 20px; /* Dodajte malo prostora oko teksta na dugmetu */
  border: none; /* Uklonite okvir dugmeta */
  font-weight: bold;
  border-radius: 3px; /* Zaobljeni rubovi za dugme */
  font-size: 15px; /* Velika veličina fonta za dugme */
  cursor: pointer; /* Prikazuje da je element interaktivan */
}

.register button:hover {
  background-color: #3a9570; /* Promenite boju dugmeta na hover */
}

.naslov {
  color: white; /* Bijela boja za naslov */
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5); /* Dodajte sjenku tekstu */
}

</style>