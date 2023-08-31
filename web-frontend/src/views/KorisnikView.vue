<template>
  <div class="autori">
    <table class="center">
      <thead>
      <tr>

        <th>ID</th>
        <th>Email Adresa</th>
        <th>Korisnicko ime</th>
        <th>Ime</th>
        <th>Prezime</th>
        <th>Datum rođenja</th>
        <th>Opis</th>
      </tr>
      </thead>
      <tbody>
      <td> {{ autorDto.id }} </td>
      <td> {{ autorDto.emailAdresa }} </td>
      <td> {{ autorDto.korisnickoIme }} </td>
      <td> {{ autorDto.ime }} </td>
      <td> {{ autorDto.prezime }} </td>
      <td> {{ formatDate(autorDto.datumRodjenja)}} </td>
      <td> {{ autorDto.opis ? autorDto.opis : '/' }} </td>
      </tbody>
    </table>

    <div id="azurirajAutora-ID">
      <div class="azuriraj-container">
        <button @click="toggleProfileUpdate" class="azuriraj">Ažuriraj profil </button>
      </div>

      <div v-if="showProfileUpdate" class="form-container">
        <h2>Ažuriraj profil:</h2>
        <label for="oldEmailAdresa">Stara Email adresa:</label>
        <input v-model="autorDto.staraEmailAdresa" /><br />
        <label for="newEmailAdresa">Nova Email adresa:</label>
        <input v-model="autorDto.novaEmailAdresa" /><br />

        <label for="ime">Ime:</label>
        <input v-model="autorDto.ime" /><br />
        <label for="prezime">Prezime:</label>
        <input v-model="autorDto.prezime" /><br />

        <label for="staraLozinka">Stara lozinka:</label>
        <input v-model="autorDto.staraLozinka" /><br />
        <label for="novaLozinka">Nova lozinka:</label>
        <input v-model="autorDto.novaLozinka" /><br />

        <label for="opis">Opis:</label>
        <input v-model="autorDto.opis" /><br />
        <label for="datumRodjenja">Datum rodjenja::</label>
        <input type="date" v-model="autorDto.datumRodjenja" /><br />

        <label for="profilnaSlika">Profilna slika:</label>
        <input v-model="autorDto.profilnaSlika" /><br />


        <button v-on:click="submit()">submit</button>
      </div>

    </div>

    <button @click="dobaviPolice" class="pogledaj-button">Korisnikove police</button>
    <div v-for="polica in policaDto" :key="polica.id" class="polica-item">
      <div>
        <strong>ID:</strong> {{ polica.id }}<br>
        <strong>Naziv:</strong> {{ polica.naziv }}<br>
        <strong>Tip:</strong> {{ polica.tip }}
      </div>
    </div>
  </div>
</template>

<script>
import AzurirajProfil from "@/components/AzurirajProfil.vue";
export default {
  name: "KorisnikView",
  components: {AzurirajProfil},
  data() {
    return {
      autorDto: {},
      updateAutorDto: {},
      showProfileUpdate: false,
      policaDto: {
        naziv: "",
        tip: "",
        knjige: [],
      },
    };
  },
  mounted: function () {

    if (this.isLoggedUserAdmin()) {
      document.getElementById("azurirajAutora-ID").style.visibility = "visible";
    } else {
      alert('Samo administrator moze vidjeti odredjene opcije!');
      document.getElementById("azurirajAutora-ID").style.visibility = "hidden";
    }

    //Get autors za azuriranje
    fetch('http://localhost:9090/api/korisnici/' + this.$route.query.id, {
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {
          console.log(data);
          this.autorDto = data
        })
        .catch((error) => {
          console.error("Error:", error);
        });

  },

  methods: {

    toggleProfileUpdate() {
      console.log("Toggle Profile Update method called");
      this.showProfileUpdate = !this.showProfileUpdate;
    },

    formatDate(dateString) {
      const options = {year: 'numeric', month: 'long', day: 'numeric'};
      return new Date(dateString).toLocaleDateString(undefined, options);
    },

    isLoggedUserAdmin() {
      //Pogledaj da li je admin ili ne

      //nadji koja mu je uloga:
      if(localStorage.getItem('loggedUser') == null) {
        return false;
      }
        const fieldName = 'ulogaKorisnika';
        const loggedUserObj = localStorage.getItem('loggedUser');
        let start_poz = loggedUserObj.indexOf(fieldName);
        start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
        const rest_of_obj = loggedUserObj.substring(start_poz, loggedUserObj.length);
        //console.log("rest_of_obj="+rest_of_obj);
        const end_of_value_poz = rest_of_obj.indexOf("\"");

        const ulogaKorisnika = rest_of_obj.substring(0, end_of_value_poz);

        //localStorage.setItem('loggedUser', JSON.stringify(data));
        if (ulogaKorisnika === undefined ||
            ulogaKorisnika === null ||
            ulogaKorisnika !== 'Administrator') {
          return false;
        }
      return true;
    },

    azurirajProfilAutora() {
      fetch('http://localhost:9090/api/korisnici/autor/' + this.autorDto.id, {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.updateAutorDto),
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully updated autor profile');

            } else if (res.status === 400) {
              alert('Bad request!');
            } else if (res.status === 403) {
              alert('Forbidden!');
            } else if (res.status === 404) {
              alert('Not found!');
            } else {
              alert('Failed update autor profile');
              //console.log(res);
              throw new Error('Failed update autor profile');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          })
          .then(() => {
            this.refresh();
          });
    },

    dobaviPolice() {

      fetch('http://localhost:9090/api/police/korisnik/' + this.$route.query.id + '/police', {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            this.policaDto = data
          })
          .catch((error) => {
            console.error("Error:", error);
          });

    },

    submit() {
      console.log("Submit kliknut!");
      console.log(JSON.stringify(this.autorDto));
      fetch('http://localhost:9090/api/korisnici/updateKorisnik/' + this.$route.query.id, {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.autorDto),
      })
          .then((res) => {
            if (res.ok) {
              window.location.reload();
            } else {
              console.log(res);
              console.log(JSON.stringify(this.autorDto));
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
.title {
  background-color: white;
  color: rebeccapurple;
  width: 30%;
  position: absolute;
  left: 450px;
}

.police-table {
  position: absolute;
  left: 450px;
  background-color: #3498db;
  font-weight: bold;
}


.title {
  background-color: white;
  color: rebeccapurple;
  width: 30%;
  position: absolute;
  left: 450px;
}

.polica-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: white;
  margin: 10px 0;
  border-radius: 5px;
  width: 30%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.police-table {
  position: absolute;
  left: 450px;
  background-color: #3498db;
  font-weight: bold;
  padding: 10px;
  border-radius: 5px;
}

.pogledaj-button {
  background-color: darkblue;
  color: white;
  font-weight: bold;
  position: absolute;
  top: 160px;
  left: 150px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>