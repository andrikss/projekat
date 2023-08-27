<template>

  <h1 class="title">{{ policaInfo.id }} | {{ policaInfo.naziv }} | {{ policaInfo.tip }} </h1>
  <button class= "delete" v-on:click="izbrisi(policaInfo)">Izbriši ovu policu :(</button>
  <br />

  <h2 class="title2">Knjige na polici:</h2>

  <div class="knjige">
    <table class="center">
      <thead>
      <tr>
        <th>ID</th>
        <th>Naslov</th>
        <th>ISBN</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="knjiga in policaInfo.knjige" :key="knjiga.id">
        <td>{{ knjiga.id }}</td>
        <td>{{ knjiga.naslov }}</td>
        <td>{{ knjiga.isbn }}</td>
        <button class="button" v-on:click="viewKnjiga(knjiga)">Pogledaj</button>
        <button class="button" v-on:click="izbaciSaPolice(knjiga)">Izbriši sa police</button>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'PolicaView',
  data() {
    return {
      policaInfo: {},
      selectedKnjiga: {},
    };
  },


  mounted: async function() {
    try {
      const policaId = this.$route.query.id;
      const data = await this.getPoliceAndBooks(policaId);

      console.log("Podaci o polici i knjigama:", data);
      this.policaInfo = data.polica;
      this.policaKnjige = data.knjige;
    } catch (error) {
      console.error("Error:", error);
    }
  },
  methods: {

    async getPoliceAndBooks(policaId) {
      try {
        const response = await fetch('http://localhost:9090/api/police/' + policaId);
        console.log("Odgovor sa servera:", response);
        const policaData = await response.json();

        const knjigeResponse = await fetch('http://localhost:9090/api/police/' + policaId + '/knjige');
        const knjigeData = await knjigeResponse.json();

        return {
          polica: policaData,
          knjige: knjigeData
        };
      } catch (error) {
        console.error("Greška pri dohvatanju podataka:", error);
        throw error;
      }
    },

    viewKnjiga(knjiga) {
      this.$router.push("/knjiga?id=" + knjiga.id);
    },

    getValueOfField(fieldName, objToSearchIn) {

      let start_poz = objToSearchIn.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = objToSearchIn.substring(start_poz,objToSearchIn.length);
      //console.log("rest_of_obj="+rest_of_obj);
      const end_of_value_poz = rest_of_obj.indexOf("\"");
      const value= rest_of_obj.substring(0,end_of_value_poz);
      //console.log("value="+value);

      return value;
    },


    izbrisi(policaInfo) {
      fetch('http://localhost:9090/api/police/obrisiPolicu/' + this.$route.query.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            console.log("Response from server:", res);

            if (res.ok) {
              const loggedUserObj = localStorage.getItem('loggedUser');
              const ulogaKorisnika = this.getValueOfField('ulogaKorisnika',loggedUserObj);

              if (ulogaKorisnika === 'Citalac') {
                this.$router.push('/homeCitalac');
              } else if (ulogaKorisnika === 'Autor') {
                this.$router.push('/homeAutor');
              } else if (ulogaKorisnika === 'Administrator') {
                this.$router.push('/homeAdministrator');
              } else {
                this.$router.push('/');
              }
            }
            else if (res.status === 400) {
              alert('Bad request!');
            }
            else if (res.status === 403) {
              alert('Forbidden!');
            }
            else if (res.status === 404) {
              alert('Not found!');
            }
            else {
              alert('Failed to delete polica!(check response)');
              throw new Error('deletion failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          });
    },


    izbaciSaPolice(selectedKnjiga) {
      fetch('http://localhost:9090/api/police/knjiga/' + selectedKnjiga.id +"/polica/"+this.$route.query.id, {
        method: "DELETE",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then((res) => {
            if (res.ok) {
              alert('Successfully removed knjiga from polica');
            }
            else if (res.status === 400) {
              alert('Bad request!');
            }
            else if (res.status === 403) {
              alert('Forbidden!');
            }
            else if (res.status === 404) {
              alert('Not found!');
            }
            else {
              throw new Error('Remove knjiga from polica failed');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          })
          .then( () => {
            this.refresh();
          });
    },


    refresh() {
      //Nadji knjigu
      fetch('http://localhost:9090/api/polica/' + this.$route.query.id)
          .then(response => response.json())
          .then(data => { this.policaInfo = data})
          .catch((error) => {
            console.error("Error:", error);
          });
    }

  },


};
</script>


<style scoped>
.delete {
  width: 30%;
  height: 30%;
  position: absolute;
  top: 0;
  right: 0;
  background-color: indianred;
  color: white;
  font-weight: bold;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.center{
  margin-left: 0;
}
.title {
  background-color: white;
  color: darkmagenta;
  font-weight: bold;
  width: 70%;
  height: 50%;
  display: flex;
  font-family: "Georgia Pro Cond Black";
}
.knjige {
  margin-left: 0px;
  display: flex;

}
.knjige tr:nth-child(even) {
  background-color: #ffffcc; /* Svijetlo žuta boja za parne redove */
}

.knjige tr:nth-child(odd) {
  background-color: #ffc0cb; /* Svijetlo roze boja za neparne redove */
}

.button {
  background-color: plum;
  color: deeppink;
  font-size: 14px;
  font-weight: bold;
  padding: 5px 10px;
  border: none;
  cursor: pointer;
}

.title2 {
  background-color: #ff4081;
  font-family: "Georgia Pro Black";
  width: 50%;
  color: white;
}
</style>