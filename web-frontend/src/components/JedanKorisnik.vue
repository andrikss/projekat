<template>
    <div class="user-profile">
      <table class="center custom-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Email Adresa</th>
          <th>Korisničko ime</th>
          <th>Uloga korisnika</th>
          <th>Ime</th>
          <th>Prezime</th>
          <th>Datum rođenja</th>
          <th>Opis</th>
          <th>Avatar</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ id }}</td>

            <td>{{ emailAdresa }}</td>

            <td>{{ korisnickoIme }}</td>

            <td>{{ ulogaKorisnika }}</td>

            <td>{{ ime }}</td>

            <td>{{ prezime }}</td>

            <td>{{ formatDate(datumRodjenjaParsed) }}</td>

            <td>{{ opis ? opis : '/' }}</td>

            <td>
              <img :src="profileImage" alt="profilna slika" class="avatar-image" style="width: 150px; height: 150px;">
            </td>
          </tr>
        </tbody>
      </table>




      <h3 class="naslovv">Korisnikove police:</h3>
      <div class="police-container">
    <table class="police-table">
      <tr v-for="polica in policeDto" :key="polica.id">
        <td>ID: {{ polica.id }}</td>
        <td>Naziv: {{ polica.naziv }}</td>
        <td>Tip: {{ polica.tip }}</td>
        <td>
          <button class="polica-button" v-on:click="viewPolica(polica)">View</button>
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <label for="naziv">Naziv nove police:</label>
          <input v-model="policaDto.naziv" />
        </td>
        <td>
          <button class="polica-button" v-on:click="addNewPolica()">Dodaj policu</button>
        </td>
      </tr>
    </table>
  </div>
    </div>

</template>

<script>
export default {
  name: "JedanKorisnik",
  data: function () {
    return {
      id: "",
      emailAdresa: "",
      korisnickoIme: "",
      ime: "",
      prezime: "",
      datumRodjenja: "",
      opis: "",
      profilnaSlika: "",
      ulogaKorisnika: "",
      policeDto: [],      //niz stringova sto su imena polica
      datumRodjenjaParsed: "",

      //Used for adding new polica
      policaDto: {
        naziv: "",
      },
    };
  },
  computed: {
    profileImage() {
      if (this.ulogaKorisnika === 'Administrator') {
        return require('@/assets/slike/administrator.png');
      } else if (this.ulogaKorisnika === 'Citalac') {
        return require('@/assets/slike/citalac.png');
      } else if (this.ulogaKorisnika === 'Autor') {
        return require('@/assets/slike/autor.png');
      } else {
        return require('@/assets/slike/pozadinaa.jpg'); // Slika za sve ostale uloge
      }
    },
  },
  methods: {

    formatDate(dateString) {
      const date = new Date(dateString);
      const options = { year: "numeric", month: "short", day: "numeric" };
      return date.toLocaleDateString("en-US", options);
    },


    addNewPolica() {
      fetch("http://localhost:9090/api/police/dodajPolicu", {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.policaDto),
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully added new polica');
              //Get the string representation of a JSON object
              const loggedUserObj = localStorage.getItem('loggedUser');
              const ulogaKorisnika = this.getValueOfField('ulogaKorisnika',loggedUserObj);

              if (ulogaKorisnika === 'Citalac') {
                this.$router.push('/homeCitalac');
              } else if (ulogaKorisnika === 'Autor') {
                this.$router.push('/homeAutor');
              } else if (ulogaKorisnika === 'Administrator') {
                this.$router.push('/homeAdministrator');
              } else {
                this.$router.push('/');
              }

            } else {
              throw new Error('Failed to create new polica!');
            }
          })
          .catch((err) => {
            console.log(err);
            alert('Failed to create new polica!');
          })
          .then( () => {
            this.refresh();

          } );
    },

    viewPolica(polica) {
      this.$router.push("/polica?id="+polica.id);
    },



    getValueOfField(fieldName, objToSearchIn) {
      let start_poz = objToSearchIn.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = objToSearchIn.substring(start_poz,objToSearchIn.length);
      //console.log("rest_of_obj="+rest_of_obj);
      const end_of_value_poz = rest_of_obj.indexOf("\"");
      const value= rest_of_obj.substring(0,end_of_value_poz);
      //console.log("value="+value);

      return value;
    },



    refresh() {

      console.log("refreshing!");
      fetch('http://localhost:9090/api/korisnici/korisnik', {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            this.id = data.id;
            this.emailAdresa = data.emailAdresa;
            this.korisnickoIme = data.korisnickoIme;
            this.ime = data.ime;
            this.prezime = data.prezime;
            this.datumRodjenja = data.datumRodjenja;
            this.opis = data.opis;
            this.profilnaSlika = data.profilnaSlika;
            this.ulogaKorisnika = data.ulogaKorisnika;
            this.policeDto = data.policeDto;
          })
          .catch((error) => {
            console.error("Error:", error);
          }).then( () => {
        this.datumRodjenjaParsed = new Date(this.datumRodjenja);
      });
    }

  },
  mounted: function() {

    fetch('http://localhost:9090/api/korisnici/korisnik', {
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {
          this.id = data.id;
          this.emailAdresa = data.emailAdresa;
          this.korisnickoIme = data.korisnickoIme;
          this.ime = data.ime;
          this.prezime = data.prezime;
          this.datumRodjenja = data.datumRodjenja;
          this.opis = data.opis;
          this.profilnaSlika = data.profilnaSlika;
          this.ulogaKorisnika = data.ulogaKorisnika;
          this.policeDto = data.policeDto;
        })
        .catch((error) => {
          console.error("Error:", error);
        }).then( () => {
      this.datumRodjenjaParsed = new Date(this.datumRodjenja);
    });
  },
}
</script>

<style scoped>
.user-profile {
  align-content: center;
  align-content: flex-start;
}
.naslovv {
  background-color: rgba(50, 50, 50, 0.2);
  background-color: indianred;
  color: white;
  width: 300px;
  position: center;
  font-weight: bold;
  margin-left: 325px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */
  align-self: center; /* Izmenjeno da bi se centrirao horizontalno */
  text-align: center;
  border: 3px solid #333333;
  border-radius: 50px;
  padding: 15px;
}

.avatar-image {
  width: auto; /* Automatska širina slike (prilagodit će se visini tabele) */
  vertical-align: middle; /* Vertikalno poravnanje sa sredinom ćelija u istoj vrsti */
}

.custom-table th,
.custom-table td {
  padding: 15px;
  text-align: left;
  font-weight: bold; /* Podebljani tekst */
  color: white; /* Bela boja teksta */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */
  transition: background-color 0.3s; /* Dodaj hover efekat */
}

.custom-table th {
  background-color: orangered; /* Narandžasta boja za zaglavlje */
}

.custom-table td {
  background-color: rgba(50, 50, 50, 0.4); /* Providno tamno siva boja za parne redove */
}

.custom-table {
  border: 5px solid orangered;
}

.custom-table tbody tr:hover {
  background-color: #555;
}

/* Dodajte ovaj stil za mobilne uređaje kako bi se sadržaj pravilno prikazivao */
@media (max-width: 768px) {
  .user-table td {
    flex-direction: column; /* Poredajte sadržaj u kolonu za male ekrane */
    align-items: flex-start; /* Poredajte sadržaj lijevo za male ekrane */
  }
}

.polica-button {
  background-color: plum; /* Drecavo plava boja */
  color: deeppink; /* Bijela boja teksta */
  font-size: 14px; /* Povećava veličinu fonta */
  font-weight: bold; /* Podebljan tekst */
  padding: 5px 10px; /* Povećava unutrašnji prostor dugmeta */
  border: none; /* Uklanja okvir dugmeta */
  cursor: pointer; /* Pokazivač prilikom hover-a preko dugmeta */
}


.user-table img {
  max-width: 100px;
}

.police-table button {
  background-color: indianred;
  border: 1px solid orangered;
  color: white;
  font-size: 14px;
  font-weight: bold;
  padding: 5px 10px;
  cursor: pointer;
  margin-left: 10px; /* Dodajte razmak s desne strane */
}

.police-table td {
  padding: 10px;
  display: flex;
  justify-content: space-between; /* Podešavanje razmaka između sadržaja i dugmeta "View" */
  align-items: center; /* Centralno poravnanje sadržaja i dugmeta "View" vertikalno */
}

.police-container {
  display: flex;
  justify-content: center; /* Centrirajte sadržaj horizontalno */
}

.police-table tr {
  border: 4px solid indianred;
}

.police-table {
  width: 300px;
  margin-top: 20px;
  color: white;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaj crni shadow */
  background-color: rgba(50, 50, 50, 0.4); /* Providno tamno siva boja za parne redove */
  display: flex; /* Dodajte ovu liniju za fleksibilno poravnanje */
  flex-direction: column;
}


</style>