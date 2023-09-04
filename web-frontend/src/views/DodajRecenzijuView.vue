<template>
  <div>
    <label for="tekst">Tekst:</label>
    <input v-model="recenzijaDto.tekst" /><br />
    <label for="ocjena">Ocjena:</label>
    <input v-model="recenzijaDto.ocjena" /><br />

    <label for="datumRecenzije">Datum recenzije:</label>
    <input type="date" v-model="recenzijaDto.datumRecenzije" /><br />


    <button v-on:click="addNewRecenzija()" class="dugme">Dodaj novu recenziju</button>

  </div>
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
label {
  display: block;
  font-weight: bold;
  margin-top: 10px;
  color: #FF69B4; /* Roze boja */
}

input {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid #FF69B4;
  border-radius: 4px;
}

/* Stilizacija za button element */
.dugme {
  background-color: #98FB98; /* Svijetlo zelena boja */
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
</style>