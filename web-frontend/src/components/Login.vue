<template>
  <div class="login_form">
    <!-- <h1>{{ emailAdresa }} {{ lozinka }}</h1> -->
    <label for="emailAdresa">Email adresa:</label>
    <input v-model="loginDTO.emailAdresa" /><br />
    <label for="lozinka">Lozinka:</label>
    <input  type="password" v-model="loginDTO.lozinka" /><br />
    <button v-on:click="submit()">POTVRDI</button>
  </div>
</template>

<script>
//import { METHODS } from 'http';

export default {
  name: 'Login',
  data: function() {
    return {
      loginDTO: {
        emailAdresa: "",
        lozinka: "",
      },
    };

  },
  methods: {
    submit: function() {
      fetch("http://localhost:9090/api/korisnici/login", {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.loginDTO),
      })
          .then((res) => {
            if (res.ok) {
              return res.json();
            } else {
              throw new Error('Login failed');
            }
          })
          .then((data) => {
            localStorage.setItem('loggedUser', JSON.stringify(data));
            if (data.ulogaKorisnika === 'Citalac') {
              this.$router.push('/homeCitalac');
            } else if (data.ulogaKorisnika === 'Autor') {
              this.$router.push('/homeAutor');
            } else if (data.ulogaKorisnika === 'Administrator') {
              this.$router.push('/homeAdministrator');
            } else {
              this.$router.push('/');
            }
          })
          .catch((err) => {
            console.log(err);
            alert('Login failed!');
          });
    },
  },

}
</script>






<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* Stilizacija za formu za login */
.login_form {
  text-align: center;
}

.login_form h2 {
  font-size: 24px; /* Velika veličina naslova */
  margin-bottom: 20px; /* Razmak ispod naslova */
}

.login_form label {
  display: block; /* Polje za unos će biti prikazano kao blok element, jedno ispod drugog */
  margin-bottom: 10px; /* Razmak između labela i input polja */
  font-weight: bold;
  color: white;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.2); /* Dodajte sjenku tekstu */
}

.login_form input {
  width: 100%;
  padding: 0px;
  margin-bottom: 20px; /* Dodajte razmak između input polja */
  border: 2px solid #ccc; /* Dodajte okvir za input polje */
  border-radius: 5px; /* Zaobljeni rubovi za input polje */
  font-size: 14px; /* Velika veličina fonta za unos */
}

.login_form button {
  background-color: #3a9550; /* Boja dugmeta za submit */
  color: white; /* Boja teksta na dugmetu */
  font-weight: bold;
  padding: 10px 20px; /* Dodajte malo prostora oko teksta na dugmetu */
  border: none; /* Uklonite okvir dugmeta */
  border-radius: 3px; /* Zaobljeni rubovi za dugme */
  font-size: 15px; /* Velika veličina fonta za dugme */
  cursor: pointer; /* Prikazuje da je element interaktivan */
}

.login_form button:hover {
  background-color: #3a9570; /* Promenite boju dugmeta na hover */
}

.naslov {
  color: white; /* Bijela boja za naslov */
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5); /* Dodajte sjenku tekstu */
}
</style>
  