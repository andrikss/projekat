<template>
  <div >
    <h3>Napravi novu knjigu:</h3>
    <div class="form-group">
    <label for="naslov">Naslov:</label>
    <input v-model="knjigaDto.naslov" /><br />
    </div>
    <div class="form-group">
    <label for="isbn">ISBN:</label>
    <input v-model="knjigaDto.isbn" /><br />
    </div>

    <div class="form-group">
    <label for="brojStrana">Broj strana:</label>
    <input v-model="knjigaDto.brojStrana" /><br />
    </div>

    <div class="form-group">
    <label for="opis">Opis:</label>
    <input v-model="knjigaDto.opis" /><br />
    </div>

    <div class="form-group">
    <label for="ocena">Ocjena:</label>
    <input v-model="knjigaDto.ocjena" /><br />
    </div>

    <div class="form-group">
    <label for="datumObjavljivanja">Datum objavljivanja:</label>
    <input type="date" v-model="knjigaDto.datumObjavljivanja" /><br />
    </div>

    <div class="form-group">
    <label for="selectedAutor">Autor:</label>
    <select v-model="selectedAutor">
      <option disabled value="">Please Select</option>
      <option v-for="autorDto in autorsDto" :value="autorDto">{{autorDto.emailAdresa}}</option>
    </select>
    </div>


    <button class = "dugme" v-on:click="submit()">Dodaj knjigu</button>

  </div>
</template>

<script>
export default {
  name: "DodajNovuKnjigu",
  data: function() {
    return {
      knjigaDto: {},
      autorsDto: [],
      selectedAutor: {},
    };

  },
  mounted: function() {

    fetch('http://localhost:9090/api/korisnici/listaAutora',{
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then(response => response.json())
        .then(data => {this.autorsDto = data;
          console.log(this.autorsDto);
        })
        .catch((error) => {
          console.error("Error:", error);
        });
  },


  methods: {

    populateInitialValues() {
      const userRole = "Autor"; // Zamijenite s stvarnim načinom dobavljanja uloge korisnika
      if (userRole === "Autor") {
        this.selectedAutor = this.autorsDto[0]; // Postavite početnog autora na prvi u listi
        this.knjigaDto.datumObjavljivanja = new Date().toISOString().substr(0, 10);
      }
    },

    submit() {

      let payload = this.knjigaDto;
      console.log(this.knjigaDto);
      payload['emailAdresaAutora'] = this.selectedAutor.emailAdresa;

      fetch('http://localhost:9090/api/knjige/dodajKnjigu', {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(payload),
      })
          .then((res) => {
            console.log(res);
            if (res.ok) {
              alert('Successfully created new knjiga!');
              this.$router.push('/knjige');
            } else {
              //alert('Failed to update knjiga');
              //console.log(res);
              alert('Failed to create new knjiga');
              throw new Error('Failed to create new knjiga');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });

    },
  },
}
</script>

<style scoped>
.form-group {
  margin-bottom: 4px;
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 2px;
}

.dugme {
  font-size: 14px;
  padding: 5px 5px;
  font-weight: bold;
  color: white;
  background-color: #d32f2f;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaje sjenku tekstu */
}

.dugme:hover {
  background-color: darkred;
}
</style>