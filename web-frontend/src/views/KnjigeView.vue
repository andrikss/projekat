<template>
  <div>

    <div class="knjige-table">
      <table class="center">

        <thead>
        <tr>
          <th>ID</th>
          <th>Naslov</th>
          <th>ISBN</th>
          <th>Broj Strana</th>
          <th>Datum Objavljivanja</th>
          <th>Opis</th>
          <th>Ocjena</th>
          <th>Zanr</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="knjiga in knjige" :key="knjiga.id">
          <td>{{ knjiga.id }}</td>
          <td>{{ knjiga.naslov }}</td>
          <td>{{ knjiga.isbn }}</td>
          <td>{{ knjiga.brojStrana }}</td>
          <td>{{ this.datumObjavljivanjaParsed[knjiga.id] }}</td>
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
        })
        //Parse time
        .then( () => {
          this.datumObjavljivanjaParsed = [];

          for (let i=0;i < this.knjige.length; i++) {
            this.datumObjavljivanjaParsed[this.knjige[i].id] = this.knjige[i].datumObjavljivanja;
            this.datumObjavljivanjaParsed[this.knjige[i].id] = new Date(this.datumObjavljivanjaParsed[this.knjige[i].id]);
          }

        });
  },
  methods: {
    viewKnjiga(knjiga) {
      this.$router.push("/knjiga?id=" + knjiga.id);
    },
  }
};
</script>

<style>
/* Stilizacija za gornji deo tabele */
.knjige-table th {
  background-color: #8bc34a; /* Svijetlo zelena */
  font-weight: bold;
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

/* Stilizacija za cijelu tablu */
.knjige-table td {
  background-color: lavender;
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

body {
  background-color: #5e35b1;
}

footer {
  text-align: center;
  margin-top: 40px;
  font-weight: bold;
}

.knjige-table,
.korisnici-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.knjige-table th,
.knjige-table td,
.korisnici-table th,
.korisnici-table td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.knjige-table th,
.korisnici-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.knjige-table .view-button {
  background-color:  #ff4081;
  padding: 8px 14px;
  text-decoration: none;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 8px;
  color: white;
}

.knjige-table tbody tr:hover,
.korisnici-table tbody tr:hover {
  background-color: #f5f5f5;
}

table.center {
  margin-left: auto;
  margin-right: auto;
}


.knjige-table .dodaj_knjigu button {
  background-color: deeppink;
  padding: 8px 14px;
  text-decoration: none;
  cursor: pointer;
  border-radius: 8px;
  color: black;
  margin-top: 15px;
}
</style>
