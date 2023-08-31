<template>
  <div>
    <div class="details-table">
      <h2 class="delete-title">Detalji knjige:</h2>
      <table class="center">
        <tbody>
        <tr>
          <td>ID:</td>
          <td>{{ knjiga.id }}</td>
        </tr>
        <tr>
          <td>Naslov:</td>
          <td>{{ knjiga.naslov }}</td>
        </tr>
        <tr>
          <td>ISBN:</td>
          <td>{{ knjiga.isbn }}</td>
        </tr>
        <tr>
          <td>Broj strana:</td>
          <td>{{ knjiga.brojStrana }}</td>
        </tr>
        <tr>
          <td>Opis:</td>
          <td>{{ knjiga.opis }}</td>
        </tr>
        <tr>
          <td>Ocena:</td>
          <td>{{ knjiga.ocjena }}</td>
        </tr>
        <tr>
          <td>Datum objavljivanja:</td>
          <td>{{ knjigaDatumObjavljivanja }}</td>
        </tr>
        </tbody>
      </table>
    </div>


      <div class="zanrovi-table">
        <h2 class="delete-title">Žanrovi knjige:</h2>
        <table class="center">
          <tbody>
          <tr v-for="zanr in knjiga.zanrovi" :key="zanr.id">
            <td>{{ zanr.naziv }}</td>
          </tr>
          </tbody>
        </table>
      </div>

    <table class="zanrovi-table">
      <thead>
      <h2 class="delete-title">Dodijeli ili izbriši žanr na ovoj knjizi:</h2>
      <tr>
        <th>Žanr</th>
        <th>Dodaj</th>
        <th>Izbriši</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="zanrDto in zanrDto" :key="zanrDto.id">
        <td>{{ zanrDto.naziv }}</td>
        <td><button class="delete-button" v-on:click="addZanrToKnjiga(zanrDto)">Dodaj</button></td>
        <td><button class="delete-button" v-on:click="removeZanrFromKnjiga(zanrDto)">Izbriši</button></td>
      </tr>
      </tbody>
    </table>

  </div>


    <div class="recenzije-wrapper">
      <h2 class="delete-title">Recenzije na ovu knjigu:</h2>
    <table class="recenzije-table">
      <tbody>
      <tr v-for="recenzija in knjiga.recenzije" :key="recenzija.id">
        <td class="recenzija-cell">{{ recenzija.tekst }}</td>
        <td class="pregled-cell">
          <div class="pregled-button-wrapper">
          <button class="recenzija-view-button" v-on:click="viewRecenzija(recenzija)">Detalji</button>
            <button class="recenzija-view-button" v-on:click="deleteRecenzija(recenzija)">Izbriši</button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <button class="dodaj-recenziju-button" v-on:click="addRecenzija">Dodaj novu recenziju</button>
    <br />
    </div>

    <div class="azuriraj-form">
      <h2 class="delete-title">Ažuriraj knjigu: </h2>
      <label for="isbn">ISBN:</label>
      <input class="input-field" v-model="azurirajKnjiguDto.isbn" /><br />

      <label for="naslov">Naslov:</label>
      <input class="input-field" v-model="azurirajKnjiguDto.naslov" /><br />

      <label for="brojStrana">Broj Strana:</label>
      <input class="input-field" v-model="azurirajKnjiguDto.brojStrana" /><br />

      <label for="opis">Opis:</label>
      <textarea class="input-field" v-model="azurirajKnjiguDto.opis"></textarea><br />

      <label for="ocena">Ocjena:</label>
      <input class="input-field" v-model="azurirajKnjiguDto.ocjena" /><br />

      <label for="selectedAutor">Autor:</label>
      <select class="input-field" v-model="selectedAutor">
        <option disabled value="">Please Select</option>
        <option v-for="autorDto in autorDto" :value="autorDto">{{ autorDto.emailAdresa }}</option>
      </select><br />

      <label for="datumObjavljivanja">Datum objavljivanja:</label>
      <input class="input-field" type="date" v-model="azurirajKnjiguDto.datumObjavljivanja" /><br />

      <button class="azuriraj-button" v-on:click="updateKnjiga(azurirajKnjiguDto)">Ažuriraj knjigu</button>
    </div>
    <br />

    <div class="add-to-shelf-section">
      <h3 class="add-to-shelf-title">Dodaj knjigu na policu:</h3>
      <table class="add-to-shelf-table">
        <tbody>
        <tr>
          <td>
            <select class="shelf-select" v-model="selectedPolica">
              <option disabled value="">Molimo odaberite</option>
              <option v-for="policaDto in policeDto" :value="policaDto">{{ policaDto.naziv }}</option>
            </select>
          </td>
          <td><button class="add-to-shelf-button" v-on:click="dodajNaPolicu(selectedPolica)">Dodaj na policu</button></td>
        </tr>
        </tbody>
      </table>
    </div>

  <div class="delete-section">
    <h2 class="delete-title">Izbriši knjigu:</h2>
    <table class="delete-table">
      <tbody>
      <tr>
        <td><button class="delete-button" v-on:click="izbrisi(knjiga)">Delete! :(</button></td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script>

import axios from 'axios';
import router from "@/router";

export default {
  name: 'KnjigaView',
  data() {
    return {
      knjiga: {},
      policeDto: [],
      selectedPolica: {},
      azurirajKnjiguDto: {},
      autorDto: [],
      selectedAutor: {},
      zanrDto: [],
      responseString: {},
      knjigaDatumObjavljivanja: {},
      zanr: ""
    };
  },

  mounted: function() {

    fetch(`http://localhost:9090/api/knjige/${this.$route.query.id}`)
        .then(response => response.json())
        .then(data => { this.knjiga = data})
        .catch((error) => {
          console.error("Error:", error);
        })
        .then( () => {
          this.knjigaDatumObjavljivanja = new Date(this.knjiga.datumObjavljivanja);
        } );

    fetch('http://localhost:9090/api/police/lista',{
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {this.policeDto = data})
        .catch((error) => {
          console.error("Error:", error);
        });

    fetch('http://localhost:9090/api/korisnici/listaAutora',{
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

    fetch('http://localhost:9090/api/zanrovi/lista',{
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {this.zanrDto = data})
        .catch((error) => {
          console.error("Error:", error);
        });



  },
  methods: {

    addZanrToKnjiga(zanrDto) {
      fetch('http://localhost:9090/api/knjige/' + this.$route.query.id +  '/zanr/'  + zanrDto.id, {
        method: "PUT",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully added zanr to knjiga!');
              window.location.reload();
            }
            else if (res.status === 400) {
              alert('Bad request!');
            }
            else if (res.status === 403) {
              alert('Forbidden!');
            }
            else if (res.status === 404) {
              console.log(res);
              alert('Not found!');
            }
            else {
              console.log(res);
              alert('Failed to add zanr to knjiga');
              throw new Error('Failed to add zanr to knjiga');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          }).then(() => {
        this.refresh();
      });

    },

    removeZanrFromKnjiga(zanrDto) {
      fetch('http://localhost:9090/api/knjige/' + this.$route.query.id +  '/zanr/'  + zanrDto.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully removed zanr from knjiga!');
              window.location.reload();
            }
            else if (res.status === 400) {
              alert('Bad request!');
            }
            else if (res.status === 403) {
              alert('Forbidden!');
            }
            else if (res.status === 404) {
              console.log(res);
              alert('Not found!');
            }
            else {
              //alert('Failed to update knjiga');
              console.log(res);
              alert('Failed to remove zanr from knjiga');
              throw new Error('Failed to remove zanr from knjiga!');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          }).then(() => {
        this.refresh();
      });
    },




    refresh() {
      fetch('http://localhost:9090/api/knjige/' + this.$route.query.id)
          .then(response => response.json())
          .then(data => { this.knjiga = data})
          .catch((error) => {
            console.error("Error:", error);
          })
          .then( () => {
            this.knjigaDatumObjavljivanja = new Date(this.knjiga.datumObjavljivanja);
          } );
    },


    addRecenzija() {
      this.$router.push("/dodajRecenziju/knjiga?id="+this.$route.query.id);
    },




    viewRecenzija(recenzija) {
      this.$router.push("/recenzija?id="+recenzija.id);
    },


    deleteRecenzija(recenzija) {
      fetch('http://localhost:9090/api/recenzije/' + recenzija.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Uspjesno ste izbrisali recenziju!')
              this.$router.push('/knjige');
            } else {
              console.log(res);
              alert('Failed to delete recenzija! (check response)');
              throw new Error('deletion failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },

    updateKnjiga(azurirajknjiguDto) {

      let payload = this.azurirajKnjiguDto;
      payload['emailAdresaAutora'] = this.selectedAutor.emailAdresa;

      fetch('http://localhost:9090/api/knjige/azurirajKnjigu/' + this.$route.query.id, {
        method: "PUT",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(payload),
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully updated knjiga!');
              location.reload();
            } else {
              alert('Failed to update knjiga');
              console.log(res);
              throw new Error('Failed to update knjiga');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          }).then( () => {
        this.refresh();
      } );

    },

    izbrisi(knjiga) {
      fetch('http://localhost:9090/api/knjige/obrisiKnjigu/' + this.$route.query.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Uspjesno ste izbrisali knjigu!')
              this.$router.push('/knjige');
            } else {
              console.log(res);
              alert('Failed to delete knjiga! (check response)');
              throw new Error('deletion failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },


    dodajNaPolicu(selectedPolica) {
      fetch('http://localhost:9090/api/police/knjiga/' + this.$route.query.id+"/polica/"+ selectedPolica.id, {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully added knjiga to polica');
              //Redirect
              if (selectedPolica.tip === "READ") {
                this.addRecenzija();
              }
              else {
                this.$router.push('/polica?id='+selectedPolica.id);
              }
            } else {
              alert('Failed to add knjiga to polica');
              //console.log(res);
              throw new Error('Adding knjiga to polica failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },
  }
};
</script>





<style>

footer {
  text-align: center;
  margin-top: 40px;
  font-weight: bold;
}

/* Stil za detalje knjige */
.details-table {
  margin: 40px auto;
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 70%;
}

.details-table table {
  width: 100%;
}

.details-table th,
.details-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

/* Stil za zanrove */
.zanrovi-table {
  margin: 40px auto;
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 70%;
}

.zanrovi-table table {
  width: 100%;
}

.section-title {
   font-size: 24px; /* Podesite veličinu prema želji */
   margin-top: 20px; /* Dodajte razmak iznad naslova */
 }


.zanrovi-table th,
.zanrovi-table td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.add-button {
  background-color: #ff4081;
  margin-right: 10px;
  color: white;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

.add-button:hover,
.delete-button:hover {
  opacity: 0.8;
}

.recenzije-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  margin: 2rem auto;
  justify-content: center;
  max-width: 70%;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.recenzije-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.recenzije-table th,
.recenzije-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}


.recenzija-cell {
  flex: 1; /* Zauzima sav raspoloživi prostor osim za View button */
}

.pregled-cell {
  display: flex;
  justify-content: flex-end; /* Centrira View button po vertikali */
}

.pregled-button-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.recenzija-view-button {
  background-color:  #ff4081;
  padding: 8px 14px;
  text-decoration: none;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 8px;
  color: white;
}
.dodaj-recenziju-button {
  background-color:  #ff4081;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}

.azuriraj-title {
  font-size: 24px;
  margin-top: 20px;
  text-align: center;
}

.azuriraj-form {
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 70%;
  margin: 0 auto;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
}

.azuriraj-button {
  background-color: #ff4081;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
  width: 100%;
}
/* Stil za ostale tabele */
.center {
  margin-left: auto;
  margin-right: auto;
}
.zanrovi-table {
  margin: 40px auto;
  border: 1px solid #ccc;
  padding: 10px;
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


.add-to-shelf-section {
  margin: 2rem auto;
  padding: 20px;
  max-width: 70%;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-to-shelf-title {
  font-size: 24px;
  margin-top: 0;
  text-align: center;
}

.add-to-shelf-table {
  width: 100%;
  border-collapse: collapse;
}

.shelf-select {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
  width: 100%;
}

.add-to-shelf-button {
  background-color: #ff4081;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 14px;
  cursor: pointer;
}

.add-to-shelf-button:hover {
  background-color: #ff2860;
}

.delete-section {
  margin: 2rem auto;
  padding: 20px;
  max-width: 70%;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.delete-title {
  font-size: 24px;
  margin-top: 0;
  text-align: center;
}

.delete-table {
  width: 100%;
  border-collapse: collapse;
}

.delete-button {
  background-color: #ff4081;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 14px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #ff2860;
}

.zanrovi-table {
  margin: 40px auto;
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  width: 70%;
}

</style>
