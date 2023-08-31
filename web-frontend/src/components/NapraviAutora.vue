<template>
  <div v-if="showCreateAuthor" class="create-author-fields">

    <label for="emailAdresa">Email adresa:</label>
    <input v-model="autorDto.emailAdresa" /><br />
    <label for="lozinka">Lozinka:</label>
    <input v-model="autorDto.lozinka" /><br />
    <label for="korisnickoIme">Korisničko ime:</label>
    <input v-model="autorDto.korisnickoIme" /><br />

    <!-- Optional: -->
    <label for="ime">Ime:</label>
    <input v-model="autorDto.ime" /><br />
    <label for="prezime">Prezime:</label>
    <input v-model="autorDto.prezime" /><br />

    <label for="opis">Opis:</label>
    <input v-model="autorDto.opis" /><br />
    <label for="datumRodjenja">Datum rođenja:</label>
    <input type="date" v-model="autorDto.datumRodjenja" /><br />

    <button v-on:click="submit()">submit</button>
  </div>
</template>


<script>
//import { METHODS } from 'http';

export default {
  name: 'NapraviAutora',
  props: {
    showForm: Boolean,
    showCreateAuthor: Boolean
  },
  data: function() {
    return {
      autorDto: {},
    };

  },
  mounted: function() {
    if (this.isLoggedUserAdmin() == false) {
      alert('Samo administrator moze da doda autora!');
      this.$router.push('/');
    }
  },
  methods: {

    isLoggedUserAdmin() {
      const fieldName  = 'ulogaKorisnika';
      const loggedUserObj = localStorage.getItem('loggedUser');
      let start_poz = loggedUserObj.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = loggedUserObj.substring(start_poz,loggedUserObj.length);
      const end_of_value_poz = rest_of_obj.indexOf("\"");

      const ulogaKorisnika = rest_of_obj.substring(0,end_of_value_poz);

      if (ulogaKorisnika === undefined ||
          ulogaKorisnika === null ||
          ulogaKorisnika !== 'Administrator')
      {
        return false;
      }
      return true;
    },


    submit: function() {
      fetch("http://localhost:9090/api/korisnici/kreirajAutora", {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.autorDto),
      })
          .then( (res) => {
            if (res.ok) {
              alert('Successfully added new autor!');
              window.location.reload();
            }
            else {
              throw new Error('Failed to add new autor');
            }
          })
          .catch((err) => {
            console.log("Error : " + err);
            alert(err);
          });
    },
  },

}
</script>


<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}

.create-author-fields {

}
</style>
