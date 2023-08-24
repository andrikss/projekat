<template>
  <div>
    <section class="search-section">
      <h2>Pretraži knjigu: </h2>
      <form @submit.prevent="searchKnjige">
        <input class="search-input" type="text" v-model="searchQuery" placeholder="Unesite ISBN ili naslov knjige" />
        <button class="search-button" type="submit">Pretraži</button>
      </form>
    </section>

    <div v-if="searched && knjige.length > 0">
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
            <th>Ocena</th>
            <th>Zanr</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="knjiga in knjige" :key="knjiga.id">
            <td>{{ knjiga.id }}</td>
            <td>{{ knjiga.naslov }}</td>
            <td>{{ knjiga.isbn }}</td>
            <td>{{ knjiga.brojStrana }}</td>
            <!-- <td>{{ knjiga.datumObjavljivanja }}</td> -->
            <td>{{ datumObjavljivanjaParsed[knjiga.id] }}</td>
            <td>{{ knjiga.opis }}</td>
            <td>{{ knjiga.ocjena }}</td>
            <td>
              <h6 v-for="zanr in knjiga.zanrovi" :key="zanr.id"> {{ zanr.naziv }} </h6>
            </td>
            <td>
              <button v-on="viewKnjiga(knjiga)">View</button>
            </td>

          </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PregledKnjigaView',
  data() {
    return {
      searchQuery: "",
      knjige: [],
      datumObjavljivanjaParsed: [], //stores the dates in a readable format, starts from i=1 (for(i=1; i<n+1)) since we use id in the tables that start from 1
      searched: false
    };
  },
  methods: {
    viewKnjiga(knjiga) {
      this.$router.push("/knjiga?id=" + knjiga.id);
    },

    searchKnjige() {
      let payload = {'isbn':this.searchQuery, 'naslov':this.searchQuery};

      fetch(`http://localhost:9090/api/knjige/pretraziKnjigu`, {
        method: "POST",
        // credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(payload),
      })
          .then(response => response.json())
          .then(data => { this.knjige = data;})
          .catch((error) => {
            console.error("Error:", error);
          })
          .then( () => {
            if (this.knjige.length > 0) {
              this.searched = true;
            }
            else {
              this.searched = false;
            }
          } )
          //Parse time
          .then( () => {
            this.datumObjavljivanjaParsed = [];
            //console.log("this.knjige.length="+this.knjige.length);

            for (let i=0;i < this.knjige.length; i++) {
              this.datumObjavljivanjaParsed[this.knjige[i].id] = this.knjige[i].datumObjavljivanja;
              //console.log('timePointElements[i]='+this.datumObjavljivanjaParsed[i+1]);
              this.datumObjavljivanjaParsed[this.knjige[i].id] = new Date(this.datumObjavljivanjaParsed[this.knjige[i].id]);
              //console.log('timePointElements[i]='+this.datumObjavljivanjaParsed[i]);
            }

          });
    }
  }
};
</script>

<style>

/* Stil za sekciju pretrage */
.search-section {
  background-color: purple;
  color: white;
  padding: 10px;
  border-radius: 8px;
  text-align: center;
  margin-bottom: 20px;
}

/* Stil za input polje za pretragu */
.search-input {
  background-color: white;
  color: darkslategray;
  font-size: 18px;
  padding: 10px;
  border: none;
  border-radius: 8px;
  margin-right: 10px;
}

/* Stil za pretrazi button */
.search-button {
  background-color: hotpink;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
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

.knjige-table button {
  background-color: aquamarine;
  padding: 8px 14px;
  text-decoration: none;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 8px;
  color: black;
}

.knjige-table tbody tr:hover,
.korisnici-table tbody tr:hover {
  background-color: #f5f5f5;
}

table.center {
  margin-left: auto;
  margin-right: auto;
}

.container2 li button {
  background-color: aquamarine;
  border: 2px solid rgb(36, 136, 102);
  text-decoration: none;
  cursor: pointer;
  border-radius: 8px;
  color: black;
}

.knjige-table .dodaj_knjigu button {
  background-color: aquamarine;
  padding: 8px 14px;
  text-decoration: none;
  cursor: pointer;
  border-radius: 8px;
  color: black;
  margin-top: 15px;
}
</style>
