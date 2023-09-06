<template>
  <div class="update_form" v-if="showProfileUpdate">
    <label for="oldEmailAdresa">Stara email adresa:</label>
    <input v-model="updateDTO.staraEmailAdresa" /><br />
    <label for="newEmailAdresa">Nova email adresa:</label>
    <input v-model="updateDTO.novaEmailAdresa" /><br />

    <label for="staraLozinka">Stara lozinka:</label>
    <input v-model="updateDTO.staraLozinka" /><br />
    <label for="novaLozinka">Nova lozinka:</label>
    <input v-model="updateDTO.novaLozinka" /><br />

    <label for="ime">Ime:</label>
    <input v-model="updateDTO.ime" /><br />
    <label for="prezime">Prezime:</label>
    <input v-model="updateDTO.prezime" /><br />

    <label for="opis">Opis:</label>
    <input v-model="updateDTO.opis" /><br />
    <label for="datumRodjenja">Datum rodjenja::</label>
    <input type="date" v-model="updateDTO.datumRodjenja" /><br />

    <label for="profilnaSlika">Profilna slika:</label>
    <input v-model="updateDTO.profilnaSlika" /><br />


    <button class="azurirajdugme" v-on:click="submit()">submit</button>
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

      this.updateDTO.novaEmailAdresa = this.updateDTO.novaEmailAdresa || null;
      this.updateDTO.novaLozinka = this.updateDTO.novaLozinka || null;

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
              console.log(res)
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
  margin-bottom: 40px;
  background-color: rgba(230, 70, 70, 0.2);
  color: white;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */

}

.update_form label {
  display: inline-block; /* Postavljanje labela kao inline-blok elemente */
  width: 150px; /* Fiksna širina labela */
  margin-right: 10px; /* Malo odvajanje između labela i input polja */
}

.azurirajdugme {
  background-color: firebrick;
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */
  font-size: 16px; /* Povećaj veličinu fonta */
  padding: 10px 10px; /* Povećaj padding za veću veličinu */
  width: 150px;
  cursor: pointer;
}

.azurirajdugme:hover {
  background-color: coral; /* Promijenite pozadinu na koralnu boju na hover-u */
}
</style>