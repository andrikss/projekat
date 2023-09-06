<template>
  <button @click="logout" class="logout-button">ODJAVA</button>
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
  font-size: 15px; /* Povećava veličinu fonta */
  font-weight: bold; /* Podebljava tekst */
  padding: 15px 30px; /* Povećava padding oko dugmeta */
  margin: 20px; /* Povećava margin oko dugmeta */
  background-color: darkred; /* Tamno crvena boja */
  border: 3px solid white;
  border-radius: 40px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaje sjenku tekstu */
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease; /* Dodat efekat prijelaza boje */
}

.logout-button:hover {
  background-color: transparent; /* Promijenjena boja pozadine na hover */
}
</style>