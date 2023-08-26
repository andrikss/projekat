<template>
  <div>
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
            <button v-on:click="viewAutor(autorDto)">View</button>
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
    fetch('http://localhost:9090/api/korisnici/listaAutora', {
      method: 'GET',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-type': 'application/json',
      },
    })
        .then((response) => response.json())
        .then((data) => {
          this.autorsDto = data;
        })
        .catch((error) => {
          console.error('Error:', error);
        });
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
  },
};
</script>

<style scoped>
.autori tr:nth-child(even) {
  background-color: #ffffcc; /* Svijetlo žuta boja za parne redove */
}

.autori tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}
</style>
