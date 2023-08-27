<template>
  <div class="update_form" v-if="showProfileUpdate">    <h2>Ažuriraj profil:</h2>
    <button v-on:click="submit()" v-if="showForm">submit</button>
    <label for="oldEmailAdresa">Stara Email adresa:</label>
    <input v-model="updateDTO.staraEmailAdresa" /><br />
    <label for="newEmailAdresa">Nova Email adresa:</label>
    <input v-model="updateDTO.novaEmailAdresa" /><br />

    <label for="ime">Ime:</label>
    <input v-model="updateDTO.ime" /><br />
    <label for="prezime">Prezime:</label>
    <input v-model="updateDTO.prezime" /><br />

    <label for="staraLozinka">Stara lozinka:</label>
    <input v-model="updateDTO.staraLozinka" /><br />
    <label for="novaLozinka">Nova lozinka:</label>
    <input v-model="updateDTO.novaLozinka" /><br />

    <label for="opis">Opis:</label>
    <input v-model="updateDTO.opis" /><br />
    <label for="datumRodjenja">Datum rodjenja::</label>
    <input type="date" v-model="updateDTO.datumRodjenja" /><br />

    <label for="profilnaSlika">Profilna slika:</label>
    <input v-model="updateDTO.profilnaSlika" /><br />


    <button v-on:click="submit()">submit</button>
  </div>
</template>

<script>
export default {
  name: "AzurirajProfil",
  props: {
    showForm: Boolean,
    showProfileUpdate: Boolean
  },
  data: function() {
    return {
      updateDTO: {
        staraEmailAdresa: "",
        novaEmailAdresa: "",
        ime: "",
        prezime: "",
        staraLozinka: "",
        novaLozinka: "",
        datumRodjenja: "",
        opis: "",
        profilnaSlika: "",
      },
    };
},
  mounted: function() {
    this.loadUserData();
    },
    methods: {
      toggleForm() {
        this.showForm = !this.showForm;
      },
      loadUserData() {
        fetch('http://localhost:9090/api/korisnici/korisnik', {
          method: "GET",
          credentials: 'include',
          headers: {
            Accept: "application/json",
            "Content-type": "application/json",
          },
        })
            .then(response => response.json())
            .then(data => {
              this.updateDTO.staraEmailAdresa = data.emailAdresa;
              this.updateDTO.korisnickoIme = data.korisnickoIme;
              this.updateDTO.ime = data.ime;
              this.updateDTO.prezime = data.prezime;
              this.updateDTO.datumRodjenja = data.datumRodjenja;
              this.updateDTO.opis = data.opis;
              this.updateDTO.profilnaSlika = data.profilnaSlika;
            })
            .catch((error) => {
              console.error("Error:", error);
            });
      },
    submit: function() {
      console.log("Submit kliknut!");
      console.log(JSON.stringify(this.updateDTO));
      fetch("http://localhost:9090/api/korisnici/updateKorisnik", {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.updateDTO),
      })
          .then((res) => {
            if (res.ok) {
              this.$router.push('/');
            } else {
              throw new Error('update user profile failed');
            }
          })
          .catch((err) => {
            console.log(err);
            alert('update user profile failed!');
          });
    },
  },

}
</script>

<style scoped>
.update_form {
  text-align: left; /* Poravnavanje teksta s lijeve strane */
  margin-top: 20px;
  background-color: pink;
}

.update_form label {
  display: inline-block; /* Postavljanje labela kao inline-blok elemente */
  width: 150px; /* Fiksna širina labela */
  margin-right: 10px; /* Malo odvajanje između labela i input polja */
}
</style>