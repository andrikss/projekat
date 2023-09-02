<template>
  <div class="table-container">
    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Email Adresa</th>
        <th>Telefon</th>
        <th>Poruka</th>
        <th>Datum</th>
        <th>Status</th>
        <th>Prihvati</th>
        <th>Odbij</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="zahtjevDto in zahtjeviDto" :key="zahtjevDto.id">
        <td>{{ zahtjevDto.id }}</td>
        <td>{{ zahtjevDto.emailAdresa }}</td>
        <td>{{ zahtjevDto.telefon }}</td>
        <td>{{ zahtjevDto.poruka }}</td>
        <td>{{ formatirajDatum(zahtjevDto.datum)}}</td>
        <td>{{ zahtjevDto.status }}</td>
        <td>
          <button @click="prihvatiZahtjev(zahtjevDto)" class="btn accept">Prihvati</button>
        </td>
        <td>
          <button @click="odbijZahtjev(zahtjevDto)" class="btn reject">Odbij</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: "ZahtjeviView",
  data() {
    return {
      zahtjeviDto: [],
    };
  },

  mounted: function () {
    if (this.isLoggedUserAdmin() == false) {
      alert('Only admins can view zahtevi!');
      this.$router.push('/');
    }
    fetch('http://localhost:9090/api/zahtjevi/lista', {
      method: "GET",
      credentials: 'include',
      headers: {
        Accept: "application/json",
        "Content-type": "application/json",
      },
    })
        .then()
        .then(response => response.json())
        .then(data => {
          this.zahtjeviDto = data
        })
        .catch((error) => {
          console.error("Error:", error);
        });
  },
  methods: {

    formatirajDatum(datum) {
      const options = { year: 'numeric', month: 'numeric', day: 'numeric' };
      return new Date(datum).toLocaleDateString(undefined, options);
    },

    isLoggedUserAdmin() {
      //Pogledaj da li je admin ili ne

      //nadji koja mu je uloga:
      const fieldName = 'ulogaKorisnika';
      const loggedUserObj = localStorage.getItem('loggedUser');
      let start_poz = loggedUserObj.indexOf(fieldName);
      start_poz += fieldName.length + 3; //+3 because of  ":"  part of the substring
      const rest_of_obj = loggedUserObj.substring(start_poz, loggedUserObj.length);
      //console.log("rest_of_obj="+rest_of_obj);
      const end_of_value_poz = rest_of_obj.indexOf("\"");

      const ulogaKorisnika = rest_of_obj.substring(0, end_of_value_poz);

      //localStorage.setItem('loggedUser', JSON.stringify(data));
      if (ulogaKorisnika === undefined ||
          ulogaKorisnika === null ||
          ulogaKorisnika !== 'Administrator') {
        return false;
      }
      return true;
    },

    prihvatiZahtjev(zahtjevDto) {
      fetch('http://localhost:9090/api/zahtjevi/prihvatiZahtjev/' + zahtjevDto.id, {
        method: "PUT",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        //body: JSON.stringify(this.updateDto),
      })
          .then(response => response.json())
          .then((res) => {
            //console.log("success:"+res);
            if (res.ok) {
              alert('Success!');
              window.location.reload()
            } else if (res.status === 400) {
              alert('Bad request!');
            } else if (res.status === 403) {
              alert('Forbidden!');
            } else if (res.status === 404) {
              alert('Not found!');
            } else {
              throw new Error('Failed to allow zahtev');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          }).then(() => {
            this.refresh();
           });
    },


    odbijZahtjev(zahtjevDto) {
      fetch('http://localhost:9090/api/zahtjevi/odbijZahtjev/' + zahtjevDto.id, {
        method: "PUT",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
      })
          .then(response => response.json())
          .then((res) => {
            if (res.ok) {
              alert('Success!');
              window.location.reload()
            } else if (res.status === 400) {
              alert('Bad request!');
            } else if (res.status === 403) {
              alert('Forbidden!');
            } else if (res.status === 404) {
              alert('Not found!');
            } else {
              console.log(res)
              throw new Error('Failed to deny zahtev');
            }
          })
          .catch((error) => {
            console.error("Error:", error);
          }).then(() => {
              this.refresh();
           });
    },

    refresh() {
      fetch('http://localhost:9090/api/zahtjevi/lista', {
        method: "GET",
        credentials: 'include',
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        //body: JSON.stringify(this.updateDto),
      })
          .then(response => response.json())
          .then(data => { this.zahtjeviDto = data})
          .catch((error) => {
            console.error("Error:", error);
          });
    },

  }
}
</script>

<style scoped>
.table-container {
  max-width: 100%;
  margin: 0 auto;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  border-radius: 5px;
}

th, td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #f2f2f2;
}

thead th {
  background-color: #f2f2f2;
}

.btn {
  padding: 10px 15px;
  border: none;
  cursor: pointer;
  font-weight: bold;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.btn.accept {
  background-color: #4CAF50;
  color: white;
}

.btn.accept:hover {
  background-color: #45a049;
}

.btn.reject {
  background-color: #F44336;
  color: white;
}

.btn.reject:hover {
  background-color: #d32f2f;
}
</style>