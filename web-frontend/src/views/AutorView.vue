<template xmlns="http://www.w3.org/1999/html">
  <div class = "table-container">
    <table class="orange-border">
      <thead>
      <tr>

        <th>ID</th>
        <th>Email Adresa</th>
        <th>Korisničko ime</th>
        <th>Ime</th>
        <th>Prezime</th>
        <th>Datum rođenja</th>
        <th>Opis</th>
        <th>Aktivan</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td class="orange-border">{{ autorDto.id }}</td>
        <td class="orange-border">{{ autorDto.emailAdresa }}</td>
        <td class="orange-border">{{ autorDto.korisnickoIme }}</td>
        <td class="orange-border">{{ autorDto.ime }}</td>
        <td class="orange-border">{{ autorDto.prezime }}</td>
        <td class="orange-border">{{ formatDate(autorDto.datumRodjenja) }}</td>
        <td class="orange-border">{{ autorDto.opis ? autorDto.opis : '/' }}</td>
        <td class="orange-border">{{autorDto.aktivan}}</td>
      </tr>
      </tbody>
    </table>

    <div class="button-and-title-container">
      <button @click="dobaviPolice" class="pogledaj-button">Pogledaj police</button>
    </div>
    <ul class="police-list">
      <li v-for="polica in policaDto" :key="polica.id" class="polica-item">
        <strong>ID:</strong> {{ polica.id }}<br>
        <strong>Naziv:</strong> {{ polica.naziv }}<br>
        <strong>Tip:</strong> {{ polica.tip }}
        <button @click="prikaziKnjigeNaPolici(polica.id)">Prikaži knjige</button>
      </li>
    </ul>

    <div class = "knjige">
      <KnjigeNaPolici v-if="showKnjigeNaPolici" :knjige="knjigeNaPolici" />
    </div>

    <div id="azurirajAutora-ID">
    <div  v-if= "!autorDto.aktivan" class="azuriraj-container">
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
import KnjigeNaPolici from "@/components/KnjigeNaPolici.vue";


export default {
  name: 'AutorView',
  components: {AzurirajAutora, KnjigeNaPolici},
  data() {
    return {
      autorDto: {},
      updateAutorDto: {},
      showProfileUpdate: false,
      showKnjigeNaPolici: false,
      knjigeNaPolici: [],
      policaDto: {
        naziv: "",
        tip: "",
        knjige: [],
      },
    };
  },
  mounted: function() {

    if (this.isLoggedUserAdmin()) {
      document.getElementById("azurirajAutora-ID").style.visibility = "visible";
    }
    else {
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
              console.log(this.updateAutorDto);
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

    prikaziKnjigeNaPolici(policaId) {
      // Napravite zahtev za dobijanje knjiga na odabranoj polici
      fetch(`http://localhost:9090/api/police/${policaId}/knjige`, {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            // Ovde možete prikazati knjige, na primer, dodavanjem u neki podatak ili komponentu
            console.log("Knjige na polici:", data);
            this.knjigeNaPolici = data;
            this.showKnjigeNaPolici = true;
            // Ako želite prikazati knjige na posebnoj stranici, možete koristiti router za navigaciju
            // this.$router.push(`/police/${policaId}/knjige`);
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },

    dobaviPolice() {

      fetch('http://localhost:9090/api/police/korisnik/' + this.$route.query.id + '/police', {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            this.policaDto = data;
            this.showKnjigeNaPolici = true;

          })
          .catch((error) => {
            console.error("Error:", error);
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

.table-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 50vh; /* Postavite visinu kontejnera na 50% visine ekrana */
}

.orange-border {
  border: 4px solid orangered;
  background-color: rgba(150, 150, 150, 0.6);
  border-radius: 15px;
  padding: 15px;
  color: white;
  font-weight: bold;
  width: 900px;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);
}

.pogledaj-button {
  background-color: orangered;
  color: white;
  font-weight: bold;
  font-size: 16px;
  padding: 20px 10px;
  border: orangered;
  border-radius: 5px;
  cursor: pointer;
  margin-left: 20px;
}

.police-list {
  list-style: none;
  padding: 0;
  width: 40%;
  height: 50vh;
  overflow-y: auto;
  position: fixed;
  bottom: 10%; /* Postavljanje na 70% od dna */
  left: 0; /* Postavljanje uz skroz levu stranu */
}

.polica-item {
  padding: 10px;
  background-color: rgba(150, 150, 150, 0.6);
  margin: 10px 0;
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex; /* Postavljanje elemenata u red */
  justify-content: space-between; /* Raspodela prostora između elemenata */
  align-items: center;
  flex-direction: column; /* Promenjeno na column za bolje poravnavanje teksta */}

.button-and-title-container {
  position: absolute;
  left: 30px;
  top: 100px;
}

.polica-item strong {
  margin-bottom: 5px; /* Dodajte razmak između jakih oznaka i vrednosti */
}

.polica-item button {
  background-color: orangered;
  color: white;
  font-weight: bold;
  font-size: 14px; /* Smanjio sam font za dugme */
  padding: 10px 15px; /* Smanjio sam padding dugmeta */
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.knjige {
  background-color: rgba(150, 150, 150, 0.9);
  position: absolute;
  border: 5px solid orangered;
  right: 350px;
  bottom: 250px;
  width: 250px;
  border-radius: 10px;
  color: white;
  font-weight: bold;
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5);

}
</style>
