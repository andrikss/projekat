<template>
  <div>
    <div class="autori">
      <table class="center custom-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Email Adresa</th>
          <th>Korisnicko ime</th>
          <th>Ime</th>
          <th>Prezime</th>
          <th>Datum rođenja</th>
          <th>Opis</th>
          <th>Aktivan</th>
        </tr>
        </thead>
        <tbody>
        <tr
            v-for="(autorDto, index) in autorsDto"
            :key="autorDto.id"
            :class="{ 'light-yellow-row': index % 2 === 0, 'light-purple-row': index % 2 !== 0 }"
        >
          <td>{{ autorDto.id }}</td>
          <td>{{ autorDto.emailAdresa }}</td>
          <td>{{ autorDto.korisnickoIme }}</td>
          <td>{{ autorDto.ime }}</td>
          <td>{{ autorDto.prezime }}</td>
          <td>{{ formatDate(autorDto.datumRodjenja) }}</td>
          <td>{{ autorDto.opis ? autorDto.opis : '/' }}</td>
          <td>{{ autorDto.aktivan }}</td>
          <td>
            <button class = "view-button" v-on:click="viewAutor(autorDto)">View</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AutoriView',
  data() {
    return {
      autorsDto: [],
    };
  },
  mounted: function () {
    // Get autors za azuriranje
    if (this.isLoggedUserAdmin()) {
      // Ako je korisnik ADMINISTRATOR, dohvati sve autore, uključujući i neaktivne
      this.fetchAllAuthors();
    } else {
      // Inače, dohvati samo aktivne autore
      this.fetchActiveAuthors();
    }

  },



  methods: {
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', options);
    },

    viewAutor(autorDto) {
      this.$router.push('/autor?id=' + autorDto.id);
    },

    isLoggedUserAdmin() {
      // Dohvati trenutno prijavljenog korisnika iz localStorage-a
      const loggedUserObj = localStorage.getItem('loggedUser');

      // Provjeri je li korisnik prijavljen
      if (loggedUserObj == null) {
        return false;
      }

      // Parsiraj JSON objekt
      const user = JSON.parse(loggedUserObj);

      // Provjeri ulogu korisnika
      if (user && user.ulogaKorisnika === 'Administrator') {
        return true; // Korisnik je ADMINISTRATOR
      } else {
        return false; // Korisnik nije ADMINISTRATOR
      }
    },

    fetchActiveAuthors() {
      fetch('http://localhost:9090/api/korisnici/listaAutora', {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            this.autorsDto = data;
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },

    fetchAllAuthors() {
      fetch('http://localhost:9090/api/korisnici/listaSvihAutora', {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            this.autorsDto = data;
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },
  },


};
</script>

<style scoped>
.custom-table th,
.custom-table td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  font-weight: bold; /* Podebljani tekst */
  color: white; /* Bela boja teksta */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */
  transition: background-color 0.3s; /* Dodaj hover efekat */
}

.custom-table th {
  background-color: orange; /* Narandžasta boja za zaglavlje */
}

.custom-table tr:nth-child(even) {
  background-color: rgba(50, 50, 50, 0.2); /* Providno tamno siva boja za parne redove */
}

.custom-table tr:nth-child(odd) {
  background-color: rgba(50, 50, 50, 0.4); /* Providno tamno siva boja za neparne redove */
}

.custom-table tbody tr:hover {
  background-color: #555;
}

.view-button {
  background-color: orange;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  transition: background-color 0.3s;
}

.view-button:hover {
  background-color: orangered;
}
</style>
