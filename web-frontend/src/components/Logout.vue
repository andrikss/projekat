<template>
  <button @click="logout" class="logout-button">Odjava :(</button>
</template>
<script>
export default {
  name: "Logout.vue",
  data: function() {
    return {
      text_obj: {
        txt: "",
      },
    };
  },
  methods: {
    logout() {
      fetch('http://localhost:9090/api/korisnici/logout', {
        method: 'GET',
        credentials: 'include',
      })
          .then((res) => {
            if (res.ok) {
              localStorage.removeItem('loggedUser');
              this.$router.push('/');
            } else {
              throw new Error('Logout failed');
            }
          })
          .catch((err) => {
            console.log(err);
            alert('Logout failed!');
          });
    }
  }
}
</script>

<style scoped>
.logout-button {
  font-size: 20px; /* Povećava veličinu fonta */
  font-weight: bold; /* Podebljava tekst */
  padding: 15px 30px; /* Povećava padding oko dugmeta */
  margin: 20px; /* Povećava margin oko dugmeta */
  background-color: purple; /* Tamno crvena boja */
  border: none;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease; /* Dodat efekat prijelaza boje */
}

.logout-button:hover {
  background-color: #ff6666; /* Promijenjena boja pozadine na hover */
}
</style>