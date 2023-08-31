<template>
  <div>

    <div class="korisnici-table">
      <table class="center">

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
            <button v-on:click="viewAutor(korisnikDto)">View</button>
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
.korisnici-table tr:nth-child(even) {
  background-color: #ffffcc; /* Svijetlo žuta boja za parne redove */
}

.korisnici-table tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}
</style>