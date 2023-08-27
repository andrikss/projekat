<template>
  <div class="basic_user_info">
    <div class="user-profile">
      <table class="user-table">
        <h2 class="title"> Osnovne informacije korisnika: </h2>
        <tr>
          <td>
            <h6>{{ id }}</h6>
            <div class="separator">|</div>

            <h6>{{ emailAdresa }}</h6>
            <div class="separator">|</div>

            <h6>{{ korisnickoIme }}</h6>
            <div class="separator">|</div>

            <h6>{{ ulogaKorisnika }}</h6>
            <div class="separator">|</div>

            <h6>{{ ime }}</h6>
            <div class="separator">|</div>

            <h6>{{ prezime }}</h6>
            <div class="separator">|</div>

            <h6>{{ opis ? opis : '/' }}</h6>
            <div class="separator">|</div>

            <h6>{{ formatDate(datumRodjenjaParsed) }}</h6>
            <div class="separator">|</div>
          </td>
          <td>
            <img :src="profilnaSlika" alt="profilna slika">
          </td>
        </tr>
      </table>



      <h3 class="title">Korisnikove police:</h3>
    <table class="police-table">
      <tr v-for="polica in policeDto" :key="polica.id">
        <td>
          id= {{ polica.id }} | {{ polica.naziv }} | {{ polica.tip }}
        </td>
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
  width: 100%;
  overflow-x: auto;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background-color: lightseagreen;
}

.user-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
  display: flex;
  align-items: center; /* Poredajte sadržaj vertikalno */
}

.user-table h6 {
  margin: 0;
}

.user-table img {
  max-width: 100px;
}

.separator {
  font-weight: bold;
  margin: 0 5px;
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
.user-profile {
  width: 100%;
  overflow-x: auto; /* Dodaje horizontalni scrollbar ako tabela premaši širinu ekrana */
}

.user-table {
  width: 100%;
  border-collapse: collapse;
}

.user-table {
  width: 100%;
  border-collapse: collapse; /* Spaja granice ćelija */
  background-color: lightseagreen;
  font-weight: bold;
}

.user-table img {
  max-width: 100px;
}

.police-table {
  width: 150%;
  margin-top: 20px;
  border-collapse: collapse;
  background-color: lightseagreen;
  display: flex; /* Dodajte ovu liniju za fleksibilno poravnanje */
  flex-direction: column; /* Dodajte ovu liniju za prikaz u koloni
}

.police-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
  display: flex; /* Dodajte ovu liniju za fleksibilno poravnanje */
  justify-content: center; /* Poravnavanje sadržaja horizontalno
}

.basic_user_info {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.user-table td {
  padding: 10px; /* Dodaje unutrašnji prostor oko sadržaja ćelija */
  border: 1px solid #ddd; /* Dodaje sivu ivicu oko ćelija */
}

.user-table h6 {
  margin: 0; /* Uklanja unutrašnji razmak oko naslova */
}

.polica-button {
  background-color: plum;
  color: deeppink;
  font-size: 14px;
  font-weight: bold;
  padding: 5px 10px;
  border: none;
  cursor: pointer;
}

.title {
  background-color: white;
  color: rebeccapurple;
}

.separator {
  font-weight: bold;
  margin: 0 5px; /* Add spacing around the separator */
}
</style>