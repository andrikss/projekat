<template>
  <div class="login_form">
    <!-- <h1>{{ emailAdresa }} {{ lozinka }}</h1> -->
    <h2>Back again!</h2>
    <label for="emailAdresa">Email adresa:</label>
    <input v-model="loginDTO.emailAdresa" /><br />
    <label for="lozinka">Lozinka:</label>
    <input  type="password" v-model="loginDTO.lozinka" /><br />
    <button v-on:click="submit()">submit</button>
  </div>
</template>

<script>
//import { METHODS } from 'http';

export default {
  name: 'Login',
  data: function() {
    return {
      loginDTO: {
        emailAdresa: "",
        lozinka: "",
      },
    };

  },
  methods: {
    submit: function() {
      fetch("http://localhost:9090/api/korisnici/login", {
        method: "POST",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.loginDTO),
      })
          .then((res) => {
            if (res.ok) {
              return res.json();
            } else {
              throw new Error('Login failed');
            }
          })
          .then((data) => {
            localStorage.setItem('loggedUser', JSON.stringify(data));
            if (data.ulogaKorisnika === 'Citalac') {
              this.$router.push('/homeCitalac');
            } else if (data.ulogaKorisnika === 'Autor') {
              this.$router.push('/homeAutor');
            } else if (data.ulogaKorisnika === 'Administrator') {
              this.$router.push('/homeAdministrator');
            } else {
              this.$router.push('/');
            }
          })
          .catch((err) => {
            console.log(err);
            alert('Login failed!');
          });
    },
  },

}
</script>






<!-- Add "scoped" attribute to limit CSS to this component only -->
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
</style>
  