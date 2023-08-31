<template>
  <div>
    <label for="tekst">Tekst:</label>
    <input v-model="recenzijaDto.tekst" /><br />
    <label for="ocjena">Ocena:</label>
    <input v-model="recenzijaDto.ocena" /><br />

    <label for="datumRecenzije">Datum recenzije:</label>
    <input type="date" v-model="recenzijaDto.datumRecenzije" /><br />


    <button v-on:click="addNewRecenzija()">Dodaj novu recenziju</button>

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

</style>