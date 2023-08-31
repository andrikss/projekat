<template>
  <div>
    <h2 class="list-title">Lista recenzija</h2>
    <table class="recenzije-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Tekst</th>
        <th>Ocjena</th>
        <th>Korisničko ime</th>
        <th>Datum</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(recenzija, index) in recenzije" :key="recenzija.id" :class="index % 2 === 0 ? 'light-pink-row' : 'light-yellow-row'">
        <td>{{ recenzija.id }}</td>
        <td>{{ recenzija.tekst }}</td>
        <td>{{ recenzija.ocjena }}</td>
        <td>{{ recenzija.korisnik }}</td>
        <td>{{ datumRecenzijeParsed(recenzija.datumRecenzije) }}</td>
      </tr>
      </tbody>
    </table>


  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'RecenzijaView',
  data() {
    return {
      recenzije : [],
      updateDto: { },
    };
  },
  mounted: function() {
    this.loadRecenzije();
  },

  methods:  {
    loadRecenzije() {
      axios
          .get(`http://localhost:9090/api/recenzije/lista`)
          .then((response) => {
            this.recenzije = response.data;
          })
          .catch((error) => {
            console.log(error);
          });
    },

    datumRecenzijeParsed(datumRecenzije) {
      if(datumRecenzije) {
      const date = new Date(datumRecenzije);
      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const year = date.getFullYear();

      return `${day}.${month}.${year}`;
    } else {
        return 'Nemam informacije o datumu';
      }
    },

    refresh() {
      fetch('http://localhost:9090/api/recenzije/api/recenzija/' + this.$route.query.id)
          .then(response => response.json())
          .then(data => { this.recenzijaDto = data})
          .catch((error) => {
            console.error("Error:", error);
          });
    },


    updateRecenzija() {

      fetch('http://localhost:9090/api/recenzije/api/recenzija/' + this.$route.query.id, {
        method: "PUT",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.updateDto),
      })
          .then((res) => {
            //console.log("success:"+res.status);
            if (res.ok) {
              // Clear user information from local storage or session storage
              //localStorage.removeItem('loggedUser');
              // Redirect to home
              alert('Recenzija successfully updated!');
              this.refresh();
              //this.$router.push('/');
            }
            else if (res.status === 400) {
              alert('Bad request!');
            }
            else if (res.status === 403) {
              alert('Forbidden!');
            }
            else if (res.status === 404) {
              alert('Not found!');
            }
            else {
              alert('Failed to update recenzija!');
              throw new Error('Failed to update recenzija');
            }
          })
          .catch((err) => {
            console.log(err);
          });
    },


    izbrisi(recenzijaDto) {
      fetch('http://localhost:9090/api/recenzije/' + this.$route.query.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              //console.log("success:"+res);
              alert('Recenzija sucessfully deleted!');
              this.$router.push('/');
            }
            else if (res.status === 400) {
              alert('Bad request!');
            }
            else if (res.status === 403) {
              alert('Forbidden!');
            }
            else if (res.status === 404) {
              alert('Not found!');
            }
            else {
              throw new Error('Failed to delete recenzija failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    }
  }


};
</script>





<style>

.list-title {
  background-color: white;
  color: black;
  padding: 10px;
  border-radius: 5px;
  text-align: center;
  margin-bottom: 20px;
}
.recenzije-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: white;
}

.recenzije-table th,
.recenzije-table td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.recenzija-row tr:nth-child(even) {
  background-color: #f2f2f2;
}

.recenzija-table tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}

.light-pink-row {
  background-color: #ffc0cb; /* Svijetlo roze boja za parne redove */
}

.light-yellow-row {
  background-color: #ffffcc; /* Svijetlo žuta boja za neparne redove */
}

footer {
  text-align: center;
  margin-top: 40px;
  font-weight: bold;
}

</style>
