<template>
  <div class="container">
    <h1 class="title">Dobrodošli na stranicu za kreiranje naloga autora</h1>
    <p class="description">Ako želite da napravite nalog autora, pošaljite svoje podatke administratoru i očekujte odgovor administratora na mejlu! :)</p>

    <div class="form-group">
      <label for="emailAdresa">Email adresa:</label>
      <input type="email" class="form-control" v-model="zahtjevDto.emailAdresa" />
    </div>

    <div class="form-group">
      <label for="telefon">Telefon:</label>
      <input class="form-control" v-model="zahtjevDto.telefon" />
    </div>

    <div class="form-group">
      <label for="poruka">Poruka:</label>
      <textarea class="form-control" v-model="zahtjevDto.poruka"></textarea>
    </div>

    <div class="form-group">
      <label for="datum">Datum:</label>
      <input type="text" class="form-control" v-model="zahtjevDto.datum" readonly />
    </div>


    <button class="btn btn-primary" v-on:click="submit()">Podnesi zahtjev</button>
  </div>
</template>

<script>
export default {
  name: "DodajZahtjevView",
  data() {
    return {
      zahtjevDto: {
        emailAdresa: '',
        telefon: '',
        poruka: '',
        datum: ''
      },
    };
  },
  methods: {
    // Dodajte funkciju za generisanje trenutnog datuma
    generisiDatum() {
      const danas = new Date();
      const godina = danas.getFullYear();
      const mesec = String(danas.getMonth() + 1).padStart(2, '0');
      const dan = String(danas.getDate()).padStart(2, '0');
      const formatiranDatum = `${godina}-${mesec}-${dan}`;
      this.zahtjevDto.datum = formatiranDatum;
    },
    submit() {

      fetch('http://localhost:9090/api/zahtjevi/dodajZahtjev', {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-type": "application/json",
        },
        body: JSON.stringify(this.zahtjevDto),


      })
          .then((res) => {
            if (res.ok) {
              this.generisiDatum();
              alert('Zahtev added successfully!');
              this.$router.push('/')
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
              throw new Error('Failed to add zahtev');
            }
          })
          .catch((error) => {
            alert('Failed to add zahtev');
            console.error("Error:", error);
          });
    }

  },
  created() {
    this.generisiDatum();
  }
}
</script>

<style scoped>
.container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
  background-color: #f7f7f7;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
}

.description {
  font-size: 16px;
  margin-bottom: 30px;
  color: #555;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.form-control {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  transition: border-color 0.2s;
}

.form-control:focus {
  border-color: #007bff;
  outline: none;
}

select.form-control {
  appearance: none;
}

.btn {
  padding: 10px 20px;
  font-size: 18px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn:hover {
  background-color: #0056b3;
}
</style>