<template>
  <div>

    <div class="korisnici-table">
      <table class="center custom-table">

        <thead>
        <tr>
          <th>ID</th>
          <th>Email adresa</th>
          <th>Korisničko ime</th>
          <th>Ime</th>
          <th>Prezime</th>
          <th>Datum rođenja</th>
          <th>Opis</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="korisnikDto in korisniksDto" :key="korisnikDto.id">
          <td>{{ korisnikDto.id }}</td>
          <td>{{ korisnikDto.emailAdresa }}</td>
          <td>{{ korisnikDto.korisnickoIme }}</td>
          <td>{{ korisnikDto.ime }}</td>
          <td>{{ korisnikDto.prezime }}</td>
          <td>{{ formatDate(korisnikDto.datumRodjenja) }}</td>
          <td>{{ korisnikDto.opis }}</td>
          <td>
            <button class = "view-button" v-on:click="viewAutor(korisnikDto)">View</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "KorisniciView",
  data() {
    return {
      korisniksDto: [],
    };
  },
  mounted: function() {


    //Get autors za azuriranje
    fetch('http://localhost:9090/api/korisnici/lista',{
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {this.korisniksDto = data})
        .catch((error) => {
          console.error("Error:", error);
        });

  },
  methods: {

    formatDate(dateString) {
      const date = new Date(dateString);
      const options = { year: "numeric", month: "short", day: "numeric" };
      return date.toLocaleDateString("en-US", options);
    },

    refresh() {
      //Get autors za azuriranje
      fetch('http://localhost:9090/api/korisnici/listaAutora',{
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {this.korisniksDto = data})
          .catch((error) => {
            console.error("Error:", error);
          });
    },


    viewAutor(autorDto) {
      this.$router.push("/korisnik?id="+autorDto.id);
    },

  }

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

.korisnici-table tr:nth-child(even) {
  background-color: rgba(50, 50, 50, 0.2); /* Providno tamno siva boja za parne redove */
}

.korisnici-table tr:nth-child(odd) {
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
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */
  transition: background-color 0.3s;
}

.view-button:hover {
  background-color: orangered; /* Boja za hover efekat */
}
</style>