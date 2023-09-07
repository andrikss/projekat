<template>
  <div class="recenzijica">
    <label for="tekst">Tekst:</label>
    <input v-model="recenzijaDto.tekst" /><br />
    <label for="ocjena">Ocjena:</label>
    <input v-model="recenzijaDto.ocjena" /><br />

    <label for="datumRecenzije">Datum recenzije:</label>
    <input type="date" v-model="recenzijaDto.datumRecenzije" /><br />

    <button v-on:click="addNewRecenzija()" class="dugme">Dodaj novu recenziju</button>

  </div>
  <img src="@/assets/slike/many-books.png" alt="Books" class="slika2" /><br /> <!-- Dodajte sliku ovde -->

</template>

<script>

import axios from 'axios';

export default {
  name: "DodajRecenzijuView",
  data() {
    return{
    recenzijaDto : {}
  };
},
methods: {
  addNewRecenzija() {

    fetch('http://localhost:9090/api/recenzije/knjiga/' + this.$route.query.id + '/recenzija', {
      method: "POST",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
      body: JSON.stringify(this.recenzijaDto),
    })
        .then((res) => {
          if (res.ok) {
            console.log(this.recenzijaDto)
            alert('Recenzija successfully added!');
            this.$router.push('/knjiga?id=' + this.$route.query.id);
          } else if (res.status === 400) {
            alert('Bad request!');
          } else if (res.status === 403) {
            alert('Forbidden! Ne možeš dodati knjigu koja nije na tvojoj READ polici!');
          } else if (res.status === 404) {
            alert('Not found!');
          } else {
            throw new Error('Failed to add recenzija');
          }
        })
        .catch((err) => {
          console.log(err);
          alert('Failed to add recenzija!');
        });

       }
  }
};
</script>

<style scoped>

.recenzijica {
  width: 600px;
  position: absolute;
  right: 450px;
  top: 210px;
  width: 550px;
  color: white;
  font-weight: bold;
  margin-bottom: 5px;
  background-color: rgba(100, 50, 50, 0.7);}

.label {
  display: block;
  font-weight: bold;
  margin-top: 10px;
  color: white;
  font-weight: bold;
  background-color: rgba(150, 150, 150, 0.5);
}

input {
  width: 90%;
  padding: 5px;
  margin-top: 5px;
  margin-bottom: 5px;
  border-radius: 4px;
}

/* Stilizacija za button element */
.dugme {
  background-color: indianred; /* Svijetlo zelena boja */
  color: white;
  border: none;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.slika {
  width: 520px; /* Podesite širinu slike prema vašim potrebama */
  height: auto; /* Očuva proporcije slike */
  position: absolute;
  left: 0;
  top:0;
}

.slika2 {
  position: absolute;
  top: 0;
  right: 470px;
  width: 500px;
  height: auto;
}
</style>