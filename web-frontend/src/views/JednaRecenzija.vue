<template>
  <div>
    <h2 class="list-title">Recenzija: </h2>
    <table class="recenzije-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Tekst</th>
        <th>Ocjena</th>
        <th>Korisničko ime</th>
        <th>Datum</th>
      </tr>
      </thead>
      <tbody>
        <td>{{ recenzijaDto.id }}</td>
        <td>{{ recenzijaDto.tekst }}</td>
        <td>{{ recenzijaDto.ocjena }}</td>
        <td>{{ recenzijaDto.korisnik }}</td>
        <td>{{ datumRecenzijeParsed(recenzijaDto.datumRecenzije) }}</td>
      </tbody>
    </table>

    <div class="azuriraj-container">
      <button @click="toggleProfileUpdate" class="azuriraj">Ažuriraj recenziju</button>
      <AzurirajRecenziju :showProfileUpdate="showProfileUpdate" />
    </div>

    <button class="izbrisi-button" v-on:click="deleteRecenzija(recenzijaDto)">Izbriši</button>

  </div>
</template>

<script>
import axios from 'axios';
import AzurirajRecenziju from "@/components/AzurirajRecenziju.vue";
export default {
  name: "JednaRecenzija",
  components: {
    AzurirajRecenziju
  },
  data() {
    return {
      recenzijaDto : {},
      showProfileUpdate : false
    };
  },
  mounted: function() {

    fetch(`http://localhost:9090/api/recenzije/${this.$route.query.id}`)
        .then(response => response.json())
        .then(data => {
          this.recenzijaDto = data
        })
        .catch((error) => {
          console.error("Error:", error);
        })
  },
  methods: {
    toggleProfileUpdate() {
      console.log("Toggle Profile Update method called");
      this.showProfileUpdate = !this.showProfileUpdate;
    },

    datumRecenzijeParsed(datumRecenzije) {
      if(datumRecenzije) {
        const date = new Date(datumRecenzije);
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear();

        return `${day}.${month}.${year}`;
      } else {
        return 'Nemam informacije o datumu';
      }
    },
    deleteRecenzija(recenzijaDto) {
      fetch('http://localhost:9090/api/recenzije/' + this.$route.query.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Uspjesno ste izbrisali recenziju!')
              this.$router.push('/knjige');
            } else {
              console.log(res);
              alert('Failed to delete recenzija! (check response)');
              throw new Error('deletion failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },
  }
}
</script>

<style scoped>

.azuriraj {
  background-color: #007bff;
  color: white;
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.azuriraj:hover {
  background-color: #0062c7;
}
.izbrisi-button {
  background-color: crimson;
  color: white;
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.izbrisi-button:hover {
  background-color: coral;
}
</style>