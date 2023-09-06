<template>
  <div>

      <h2 class="list-title">Lista žanrova:</h2>
      <table class="zanr-table orange-border">

          <thead>
          <tr>
            <th class="top-row">ID</th>
            <th class="top-row">Naziv</th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="(zanr, index) in zanrs"
              :key="zanr.id"
              :class="index % 2 === 0 ? 'light-yellow-row' : 'light-pink-row'"
          >
            <td>{{ zanr.id }}</td>
            <td>{{ zanr.naziv }}</td>

          </tr>
          </tbody>
      </table>
    </div>

  <div class="add-genre-section" id="dodajNoviZanrID">
    <h2 class="add-genre-title">Dodaj novi žanr:</h2>
    <label for="addNewZanrDto">Žanr:</label>
    <input v-model="newZanrName" /><br />
    <button class="add-genre-button" @click="addNewZanr">Dodaj žanr</button>
  </div>



</template>



<script>
import axios from 'axios';

export default {
  name: 'ZanroviView',
  data() {
    return {
      zanrs: [],
      newZanrName: "",
    };
  },
  mounted: function() {

    this.loadZanrovi();

    if (this.isLoggedUserAdmin()) {
      document.getElementById("dodajNoviZanrID").style.visibility = "visible";
    }
    else {
      document.getElementById("dodajNoviZanrID").style.visibility = "hidden";
    }


  },
  methods: {


    addNewZanr() {
      fetch('http://localhost:9090/api/zanrovi/dodajZanr', {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify({ naziv: this.newZanrName }),
      })
          .then((res) => {
            if (res.ok) {
              alert('Uspjesno dodat novi zanr!');
              window.location.reload();
            } else {
              console.log(res);
              alert('Greska!');
              //throw new Error('Adding knjiga to polica failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },

    loadZanrovi() {
      axios
          .get(`http://localhost:9090/api/zanrovi/lista`)
          .then((response) => {
            this.zanrs = response.data;
          })
          .catch((error) => {
            console.log(error);
          });
    },

    isLoggedUserAdmin() {
      //Pogledaj da li je admin ili ne
    if(localStorage.getItem('loggedUser') == null) {
      return false;
    }
      //nadji koja mu je uloga:
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


    refresh() {
      //Nadji sve zanrove
      fetch('http://localhost:9090/api/zanrovi/lista')
          .then(response => response.json())
          .then(data => {
            this.zanrs = data
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },

  }
};
</script>





<style>

.orange-border {
  border: 4px solid orangered;
  background-color: rgba(150, 150, 150, 0.2);
  border-radius: 15px;
  padding: 20px;
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);
  width: 100%;
}

.list-title {
  background-color: white;
  color: black;
  padding: 10px;
  border-radius: 5px;
  text-align: center;
  margin-bottom: 20px;
}



.add-genre-section {
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 70%;
}

.add-genre-title {
  font-size: 24px;
  margin-top: 0;
  text-align: center;
}

.add-genre-button {
  background-color: #ff4081;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 10px;
}

.zanr-table td {
  background-color: rgba(150, 150, 150, 0.6); /* Boja za redove tabele */
  color: white;
  padding: 10px;
  text-align: left;
   /* Granica oko redova */
  border-bottom: 1px solid white; /* Linija ispod redova */
}

/* Hover efekat za redove tabele */
.zanr-table tbody tr:hover {
  background-color: darkorange; /* Boja za hover efekat */
}
</style>
