<template>
  <div class="update_form" v-if="showProfileUpdate">    <h2>Ažuriraj profil:</h2>
    <button v-on:click="submit()" v-if="showForm">submit</button>

    <label for="tekst">Tekst:</label>
    <input v-model="updateDTO.tekst" /><br />
    <label for="ocjena">Ocjena:</label>
    <input v-model="updateDTO.ocjena" /><br />
    <label for="datumRecenzije">Datum recenzije:</label>
    <input type="date" v-model="updateDTO.datumRecenzije" /><br />


    <button v-on:click="submit()">SUBMIT</button>
  </div>
</template>

<script>
export default {
  name: "AzurirajRecenziju",
  props: {
    showForm: Boolean,
    showProfileUpdate: Boolean
  },
  data: function() {
    return {
      updateDTO: {
        tekst: "",
        ocjena: "",
        datumRecenzije: ""
      }
    };
},

  methods: {
    toggleForm() {
      this.showForm = !this.showForm;
    },
    submit: function () {

      console.log("Submit kliknut!");
      console.log(JSON.stringify(this.updateDTO));
      fetch('http://localhost:9090/api/recenzije/' + this.$route.query.id, {
        method: "PUT",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.updateDTO),
      })
          .then((res) => {
            if (res.ok) {
              this.$router.push('/recenzije');
            } else {
              console.log(res);
              throw new Error('update recenzija failed');
            }
          })
          .catch((err) => {
            console.log(err);
            alert('update user profile failed!');
          });
    },
  }
}
</script>

<style scoped>
.update_form {
  background-color: white;
  width: 500px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

h2 {
  text-align: center;
}

label {
  margin-bottom: 10px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  background-color: #007bff;
  color: white;
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #0062c7;
}
</style>