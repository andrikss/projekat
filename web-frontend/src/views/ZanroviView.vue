<template>
  <div>

    <div >
      <h2 class="list-title">Lista žanrova:</h2>
      <div class="zanr-table">
        <table class="center custom-table">

          <thead>
          <tr>
            <th class="white-background">ID</th>
            <th class="white-background">Naziv</th>
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
    </div>

  </div>

  <!-- will be hidden for non admin users -->
  <div id="dodajNoviZanrDiv">
    <h2>Dodaj novi zanr:</h2>
    <label for="naziv">naziv:</label>
    <input v-model="addNewZanrDto.naziv" /><br />

    <button v-on:click="addNewZanr()">Dodaj novi zanr</button>
  </div>

</template>



<script>
import axios from 'axios';

export default {
  name: 'ZanroviView',
  data() {
    return {
      zanrs: [],
      addNewZanrDto: {},
    };
  },
  mounted: function() {

    this.loadZanrovi();

    if (this.isLoggedUserAdmin()) {
      document.getElementById("dodajNoviZanrDiv").style.visibility = "visible";
    }
    else {
      document.getElementById("dodajNoviZanrDiv").style.visibility = "hidden";
    }


  },
  methods: {

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

      //nadji koja mu je uloga:
      const fieldName  = 'ulogaKorisnika';
      const loggedUserObj = localStorage.getItem('loggedUser');
      let start_poz = loggedUserObj.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = loggedUserObj.substring(start_poz,loggedUserObj.length);
      //console.log("rest_of_obj="+rest_of_obj);
      const end_of_value_poz = rest_of_obj.indexOf("\"");

      const ulogaKorisnika = rest_of_obj.substring(0,end_of_value_poz);

      //localStorage.setItem('loggedUser', JSON.stringify(data));
      if (ulogaKorisnika === undefined ||
          ulogaKorisnika === null ||
          ulogaKorisnika !== 'Administrator')
      {
        return false;
      }
      return true;
    },



    refresh() {
      //Nadji sve zanrove
      fetch('http://localhost:9090/api/zanrovi/lista')
          .then(response => response.json())
          .then(data => { this.zanriDto = data})
          .catch((error) => {
            console.error("Error:", error);
          });
    },


    addNewZanr() {

      fetch("http://localhost:9090/api/zanrovi/dodajZanr", {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.addNewZanrDto),
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully added new zanr!');
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
              //alert('Failed to update knjiga');
              //console.log(res);
              throw new Error('Failed to add new zanr');
            }
          })
          .catch((err) => {
            console.log("Error : " + err);
            alert(err);
          }).then( () => {
        this.refresh();
      } );
    },
  },
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
.custom-table {
  width: 80%;
  margin: 20px auto;
  border-collapse: collapse;
}

.custom-table th,
.custom-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.highlight-id {
  background-color: deeppink; /*  boja za parne redove u koloni ID */
  color: deeppink;
}

footer {
  text-align: center;
  margin-top: 40px;
  font-weight: bold;
}


table.center {
  margin-left: auto;
  margin-right: auto;
}


.zanr-table tr:nth-child(even) {
  background-color: #ffffcc; /* Svijetlo žuta boja za parne redove */
}

.zanr-table tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}

.white-background {
  background-color: white; /* Bijela pozadina za ID i Naziv */
}
</style>
