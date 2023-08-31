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
      <td> {{ autorDto.id }} </td>
      <td> {{ autorDto.emailAdresa }} </td>
      <td> {{ autorDto.korisnickoIme }} </td>
      <td> {{ autorDto.ime }} </td>
      <td> {{ autorDto.prezime }} </td>
      <td> {{ formatDate(autorDto.datumRodjenja)}} </td>
      <td> {{ autorDto.opis ? autorDto.opis : '/' }} </td>
      <td> {{ autorDto.aktivan }} </td>
      </tbody>
    </table>

    <div id="azurirajAutora-ID">
    <div class="azuriraj-container">
      <button @click="toggleProfileUpdate" class="azuriraj">Ažuriraj profil </button>
    </div>

      <div v-if="showProfileUpdate" class="form-container">
        <AzurirajAutora :showForm="showProfileUpdate" :showProfileUpdate="showProfileUpdate" />
      </div>

    </div>

  </div>
</template>

<script>

import AzurirajAutora from "@/components/AzurirajAutora.vue";

export default {
  name: 'AutorView',
  components: {AzurirajAutora},
  data() {
    return {
      autorDto: {},
      updateAutorDto: {},
      showProfileUpdate: false
    };
  },
  mounted: function() {

    if (this.isLoggedUserAdmin()) {
      document.getElementById("azurirajAutora-ID").style.visibility = "visible";
    }
    else {
      alert('Samo admini i vlasnik acc-a moze da vidi jos opcija za nalog autora!');
      document.getElementById("azurirajAutora-ID").style.visibility = "hidden";
    }

    //Get autors za azuriranje
    fetch('http://localhost:9090/api/korisnici/autor/'+ this.$route.query.id,{
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

    toggleProfileUpdate() {
      console.log("Toggle Profile Update method called");
      this.showProfileUpdate = !this.showProfileUpdate;
    },

    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },

    isLoggedUserAdmin() {
      //Pogledaj da li je admin ili ne
      if(localStorage.getItem('loggedUser') == null) {
        return false;
      }
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
      fetch('http://localhost:9090/api/korisnici/updateAutor/' + this.autorDto.id, {
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
              window.location.reload();
              alert('Successfully updated autor profile');

            } else if (res.status === 400) {
              alert('Bad request!');
            } else if (res.status === 403) {
              alert('Forbidden!');
            } else if (res.status === 404) {
              alert('Not found!');
            } else {
              alert('Failed update autor profile');
              //console.log(res);
              throw new Error('Failed update autor profile');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          })
          .then(() => {
            this.refresh();
          });
    },



    refresh() {
      //Get autors za azuriranje
      fetch('http://localhost:9090/api/korisnici/autor/'+ this.$route.query.id,{
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


  }
};
</script>





<style>
.azuriraj {
  background-color: darkorange; /* Tamno narandžasta boja */
  color: white; /* Bijela boja teksta */
  font-size: 16px;
  font-weight: bold;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  margin: 0;
  transition: background-color 0.3s; /* Dodaj tranziciju za glatku promenu boje */
}

.azuriraj:hover {
  background-color: lightyellow; /* Svijetlo žuta boja na hover */
  color: black;
}

.azuriraj-container {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.autori tr:nth-child(even) {
  background-color: #ffffcc; /* Svijetlo žuta boja za parne redove */
}

.autori tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}

.center {
  background-color: lightskyblue;
  font-weight: bold;
}


.form-container {
  background-color: orange;
  padding: 10px;
  border-radius: 10px;
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;
  position: absolute;
  left: 420px;
}

.form-container label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  text-align: center;
}

.form-container input {
  width: 50%;
  max-width: 300px; /* Definišite maksimalnu širinu polja */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
}

.form-container button {
  background-color: darkorange;
  width: 50%;
  color: white;
  font-size: 14px;
  font-weight: bold;
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.form-container button:hover {
  background-color: lightyellow;
}
</style>
