<template>
  <div>
    <section class="search-section">
      <h2 class="title">Pretraži knjigu: </h2>
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
.title {
  border: 4px solid orangered;
  background-color: rgba(150, 150, 150, 0.6);
  border-radius: 15px;
  padding: 20px;
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);
}
.search-section {
  background-image: url('@/assets/slike/pozadinaa.jpg');
  background-size: cover;
  background-position: center;
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
  padding: 20px;
  width: 250px;
  border: none;
  border-radius: 8px;
  margin-right: 10px;
  margin-bottom: 20px;
}

/* Stil za pretrazi button */
.search-button {
  background-color: orangered;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: darkorange;
}
</style>
