<template xmlns="http://www.w3.org/1999/html">
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

      </tr>
      </tbody>
    </table>


    <div id="azuriranjeAutoraDivId">
      <h2>Azuziranje autora</h2>
      <label for="emailAdresa">emailAdresa:</label>
      <input v-model="updateAutorDto.emailAdresa" /><br />
      <label for="korisnickoIme">korisnickoIme:</label>
      <input v-model="updateAutorDto.korisnickoIme" /><br />
      <label for="ime">ime:</label>
      <input v-model="updateAutorDto.ime" /><br />
      <label for="prezime">prezime:</label>
      <input v-model="updateAutorDto.prezime" /><br />
      <label for="opis">opis:</label>
      <input v-model="updateAutorDto.opis" /><br />
      <label for="datumRodjenja">datumRodjenja:</label>
      <input type="date" v-model="updateAutorDto.datumRodjenja" /><br />
      <button v-on:click="azurirajProfilAutora()">Azuriraj profil autora</button> <br />
    </div>

  </div>
</template>

<script>

export default {
  name: 'AutorView',
  data() {
    return {
      autorDto: {},
      updateAutorDto: {},
    };
  },
  mounted: function() {

    if (this.isLoggedUserAdmin()) {
      document.getElementById("azuriranjeAutoraDivId").style.visibility = "visible";
    }
    else {
      alert('Samo admini i vlasnik acc-a moze da vidi jos opcija za nalog autora!');
      document.getElementById("azuriranjeAutoraDivId").style.visibility = "hidden";
    }

    //Get autors za azuriranje
    fetch('http://localhost:9090/api/korisnici/'+ this.$route.query.id,{
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {this.autorDto = data})
        .catch((error) => {
          console.error("Error:", error);
        });

  },
  methods: {

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

    azurirajProfilAutora() {
      fetch('http://localhost:9090/api/autor/' + this.autorDto.id, {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.updateAutorDto),
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully updated autor profile');

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
              alert('Failed update autor profile');
              //console.log(res);
              throw new Error('Failed update autor profile');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          })
          .then( () => {
            this.refresh();
          } );
    },



    refresh() {
      //Get autors za azuriranje
      fetch('http://localhost:9090/api/autor/'+ this.$route.query.id,{
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {this.autorDto = data})
          .catch((error) => {
            console.error("Error:", error);
          });
    },





    // addRecenzija() {
    //     this.$router.push("/add-recenzija/knjiga?id="+this.$route.query.id);
    // },
    // viewRecenzija(recenzija) {
    //     this.$router.push("/recenzija?id="+recenzija.id);
    // },




  }
};
</script>





<style>
.autori tr:nth-child(even) {
  background-color: #ffffcc; /* Svijetlo žuta boja za parne redove */
}

.autori tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}
</style>
