<template>
  <div class="register">

    <h2>Postani naš član!</h2>
    <label for="emailAdresa">Email adresa:</label>
    <input v-model="registrujseDTO.emailAdresa" /><br />
    <label for="lozinka">Lozinka:</label>
    <input type="password" v-model="registrujseDTO.lozinka" /><br />
    <label for="ponovljenaLozinka">Ponovljena lozinka:</label>
    <input type="password" v-model="registrujseDTO.ponovljenaLozinka" /><br />
    <label for="korisnickoIme">Korisničko ime:</label>
    <input v-model="registrujseDTO.korisnickoIme" /><br />

    <label for="ime">Ime:</label>
    <input v-model="registrujseDTO.ime" /><br />
    <label for="prezime">Prezime:</label>
    <input v-model="registrujseDTO.prezime" /><br />
    <label for="datumRodjenja">Datum rođenja:</label>
    <input type="date" v-model="registrujseDTO.datumRodjenja" /><br />


    <button v-on:click="submit()">submit</button>
  </div>
</template>

<script>
export default {
  name: "Register",
  data: function() {
    return {
      registrujseDTO: {
        emailAdresa: "",
        lozinka: "",
        ponovljenaLozinka: "",
        korisnickoIme: "",
        ime: "",
        prezime: "",
        datumRodjenja: "",
        opis: "",

      },
    };
  },
  methods: {
    getValueOfField(fieldName, objToSearchIn) {

      let start_poz = objToSearchIn.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = objToSearchIn.substring(start_poz, objToSearchIn.length);
      //console.log("rest_of_obj="+rest_of_obj);
      const end_of_value_poz = rest_of_obj.indexOf("\"");
      const value = rest_of_obj.substring(0, end_of_value_poz);
      //console.log("value="+value);

      return value;
    },

    submit: function () {
        console.log(JSON.stringify(this.registrujseDTO))
        fetch("http://localhost:9090/api/korisnici/register", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-type": "application/json",
          },
          body: JSON.stringify(this.registrujseDTO),
        })

      .
        then((res) => {
          //console.log("success:"+res);
          if (res.ok) {
            console.log('Successfully created new citalac!');
            return res.json();
          } else if (res.status === 400) {
            alert('Bad request!');
          } else if (res.status === 403) {
            alert('Forbidden!');
          } else if (res.status === 404) {
            alert('Not found!');
          } else {
            console.log('Failed to create new citalac!');
            throw new Error('Login failed');
          }
        })
            .then((data) => {
              //console.log("Success : " + data);
              localStorage.setItem('loggedUser', JSON.stringify(data));
              //console.log('data='+data);

              // this.$router.push("/employees");
              // this.$router.replace("/");  //releads the page
            })
            .catch((err) => {
              console.log("Error : " + err);
              alert(err);
            })
            .then(() => {
              const loggedUserObj = localStorage.getItem('loggedUser');
              const ulogaKorisnika = this.getValueOfField('ulogaKorisnika', loggedUserObj);
              console.log('ulogaKorisnika=' + ulogaKorisnika);


              if (ulogaKorisnika === 'Citalac') {
                this.$router.push('/homeCitalac');
              } else if (ulogaKorisnika === 'Autor') {
                this.$router.push('/homeAutor');
              } else if (ulogaKorisnika === 'Administrator') {
                this.$router.push('/homeAdministrator');
              } else {
                this.$router.push('/');
              }
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
</style>