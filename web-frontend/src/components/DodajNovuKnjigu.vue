<template>
  <div >
    <h3>Kreiraj novu knjigu:</h3>
    <label for="naslov">Naslov:</label>
    <input v-model="knjigaDto.naslov" /><br />
    <label for="isbn">ISBN:</label>
    <input v-model="knjigaDto.isbn" /><br />
    <label for="brojStrana">Broj strana:</label>
    <input v-model="knjigaDto.brojStrana" /><br />
    <label for="opis">Opis:</label>
    <input v-model="knjigaDto.opis" /><br />
    <label for="ocena">Ocjena:</label>
    <input v-model="knjigaDto.ocjena" /><br />

    <label for="datumObjavljivanja">Datum objavljivanja:</label>
    <input type="date" v-model="knjigaDto.datumObjavljivanja" /><br />

    <label for="selectedAutor">Autor:</label>
    <select v-model="selectedAutor">
      <option disabled value="">Please Select</option>
      <option v-for="autorDto in autorsDto" :value="autorDto">{{autorDto.emailAdresa}}</option>
    </select>


    <button v-on:click="submit()">Add knjiga</button>

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

</style>