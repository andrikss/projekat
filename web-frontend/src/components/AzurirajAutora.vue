<template>
  <div class="update_form" v-if="showProfileUpdate">
    <h2>Ažuriranje autora:</h2>
    <div class="form-field">
    <label for="emailAdresa">Email adresa:</label>
    <input v-model="updateDTO.emailAdresa" /><br />
    </div>

    <div class="form-field">
    <label for="korisnickoIme">Korisnicko ime:</label>
    </div>

    <div class="form-field">
    <input v-model="updateDTO.korisnickoIme" /><br />
    </div>

    <div class="form-field">
    <label for="ime">Ime:</label>
    </div>

    <div class="form-field">
    <input v-model="updateDTO.ime" /><br />
    </div>

    <div class="form-field">
    <label for="prezime">Prezime:</label>
    <input v-model="updateDTO.prezime" /><br />
    </div>

    <div class="form-field">
    <label for="opis">Opis:</label>
      <input v-model="updateDTO.opis" /><br />
    </div>

    <div class="form-field">
    <label for="datumRodjenja">Datum rođenja:</label>
      <input type="date" v-model="updateDTO.datumRodjenja" /><br />
    </div>

    <div class="form-field">
      <label for="opis">Lozinka:</label>
      <input v-model="updateDTO.lozinka" /><br />
    </div>

    <div class="form-field">
      <button v-on:click="submit()" class="submit-button">Ažuriraj!</button>
    </div>
    </div>
</template>

<script>
export default {
  name: "AzurirajAutora",
  props: {
    showForm: Boolean,
    showProfileUpdate: Boolean
  },
  data: function () {
    return {
      updateDTO: {
        emailAdresa: "",
        ime: "",
        prezime: "",
        datumRodjenja: "",
        opis: "",
        profilnaSlika: "",
        lozinka: ""
      },
    };
  },
  mounted: function () {
    this.loadUserData();
  },
  methods: {
    toggleForm() {
      this.showForm = !this.showForm;
    },
    loadUserData() {
      fetch('http://localhost:9090/api/korisnici/autor/' + this.$route.query.id, {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            this.updateDTO.emailAdresa = data.emailAdresa;
            this.updateDTO.korisnickoIme = data.korisnickoIme;
            this.updateDTO.ime = data.ime;
            this.updateDTO.prezime = data.prezime;
            this.updateDTO.opis = data.opis;
            this.updateDTO.datumRodjenja = data.datumRodjenja;
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },
    submit: function () {
      console.log("Submit kliknut!");
      console.log(JSON.stringify(this.updateDTO));

      fetch("http://localhost:9090/api/korisnici/updateAutor/" + this.$route.query.id , {
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
              window.location.reload();
            } else {
              console.log(res);
              console.log(JSON.stringify(this.updateDTO));
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
  background-color: orange;
  padding: 20px;
  border-radius: 10px;
  margin: 20px auto; /* Centriranje po horizontali i margina između */
  max-width: 400px; /* Maksimalna širina 59.5% */
  display: flex;
  flex-direction: column;
  align-items: center;
}

.update_form label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  text-align: center;
}

.update_form input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
}


.update_form button {
  background-color: darkorange;
  color: white;
  font-size: 16px;
  font-weight: bold;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button {
  background-color: darkorange;
  color: white;
  font-size: 16px;
  font-weight: bold;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: lightyellow;
}

.update_form button:hover {
  background-color: lightyellow;
}
</style>