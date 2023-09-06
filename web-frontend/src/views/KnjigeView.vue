<template>
  <div>

    <div class="knjige-table">
      <table class="center">

        <thead>
        <tr>
          <th class="top-row">ID</th>
          <th class="top-row">Naslov</th>
          <th class="top-row">ISBN</th>
          <th class="top-row">Broj Strana</th>
          <th class="top-row">Datum Objavljivanja</th>
          <th class="top-row">Opis</th>
          <th class="top-row">Ocjena</th>
          <th class="top-row">Zanr</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="knjiga in knjige" :key="knjiga.id">
          <td>{{ knjiga.id }}</td>
          <td>{{ knjiga.naslov }}</td>
          <td>{{ knjiga.isbn }}</td>
          <td>{{ knjiga.brojStrana }}</td>
          <td>{{ formatDate(knjiga.datumObjavljivanja) }}</td>
          <td>{{ knjiga.opis }}</td>
          <td>{{ knjiga.ocjena }}</td>
          <td>
            <h6 v-for="zanr in knjiga.zanrovi" :key="zanr.id"> {{ zanr.naziv }} </h6>
          </td>
          <td>
            <button class="view-button" v-on:click="viewKnjiga(knjiga)">Detalji</button>
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
  name: 'KnjigeView',
  data() {
    return {
      searchQuery: '',
      knjige: [],
      datumObjavljivanjaParsed: [],
      searched: false
    };
  },
  mounted: function() {
    axios
        .get(`http://localhost:9090/api/knjige/lista`)
        .then((response) => {
          if (response.data.length === 0) {
            alert('Ne postoji knjiga sa tim imenom');
            this.searched = false;
          } else {
            this.knjige = response.data;
            this.searched = true;
          }
        })
        .catch((error) => {
          console.log(error);
          alert('Ne postoji ta knjiga');
        });
  },

  methods: {
    viewKnjiga(knjiga) {
      this.$router.push("/knjiga?id=" + knjiga.id);
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', options);
    },
  }
};
</script>

<style>

.knjige-table table {
  border: 3px solid orangered; /* Granica oko cele tabele */
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.knjige-table .top-row {
  background-color: orangered; /* Boja za gornji red tabele */
  color: white;
  font-weight: bold;
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid white; /* Linija ispod zaglavlja */
}

.knjige-table th {
  background-color: orangered; /* Boja za gornji deo tabele */
  color: white;
  font-weight: bold;
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid white; /* Linija ispod zaglavlja */
}

/* Stil za redove tabele */
.knjige-table td {
  background-color: rgba(150, 150, 150, 0.6); /* Boja za redove tabele */
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);
  padding: 10px;
  text-align: left;
  border : 3px solid orangered;
  border-bottom: 1px solid white; /* Linija ispod redova */
}

/* Hover efekat za redove tabele */
.knjige-table tbody tr:hover {
  background-color: darkorange; /* Boja za hover efekat */
}

.knjige-table .view-button {
  background-color: orangered;
  padding: 20px 14px;
  text-decoration: none;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 8px;
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);
}

.knjige-table tbody tr:hover,
.korisnici-table tbody tr:hover {
  background-color: #f5f5f5;
}

</style>
