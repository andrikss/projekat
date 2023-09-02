<template>
  <div class="home">
    <nav class="navbar">
      <a href="/pretragaKnjiga">PRETRAGA KNJIGA</a>
      <span class="separator">|</span>
      <a href="/knjige">LISTA KNJIGA</a>
      <span class="separator">|</span>
      <a href="/recenzije">LISTA RECENZIJA</a>
      <span class="separator">|</span>
      <a href="/zanrovi">LISTA ŽANROVA</a>
      <span class="separator">|</span>
      <a href="/korisnici">LISTA KORISNIKA</a>
    </nav>

    <div class="activation-request">
      <a href="/dodajZahtjev">PODNESI ZAHTJEV ZA AKTIVACIJU NALOGA AUTORA</a>
    </div>

    <div class="auth-container">
    <div class="auth-section">
      <h2 class="auth-title">Prijava</h2>
      <Login />
    </div>
    <div class="auth-section">
      <h2 class="auth-title">Registracija</h2>
      <Register />
    </div>
      </div>
  </div>


</template>

<script>
// @ is an alias to /src
import Login from '@/components/Login.vue'
import Register from '@/components/Register.vue'


export default {
  name: 'HomeView',
  components: {
    Login,
    Register,
  }
}
</script>

<style>
.home {
  text-align: center;
  padding: 20px;
  background-color: #c29eff;
}

.navbar {
  margin-bottom: 20px;
}

.navbar a {
  text-decoration: none;
  color: #333;
  font-weight: bold;
  margin-right: 10px;
}

.separator {
  margin: 0 10px;
  color: #999;
}

/* Stilizacija za komponente Login i Register */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100vh;
}

/* Stilizacija za svaku komponentu */
.auth-section {
  width: 50%;
  padding: 20px;
  box-sizing: border-box;
  margin: 10px; /* Dodajte margin za razmak između Login i Register delova */
  border: 2px solid;
  border-radius: 10px;
}

.auth-section:nth-child(1) {
  border-color: #FF1493; /* Roze boja za Login */
}

.auth-section:nth-child(2) {
  border-color: #8A2BE2; /* Ljubičasta boja za Register */
}

.auth-title {
  font-size: 20px;
  margin-bottom: 10px;
}

/* Stilizacija za zahtjev za aktivaciju */
.activation-request {
  position: fixed;
  bottom: 20px;
  left: 20px;
  padding: 10px;
  background-color: pink;
  color: white;
  font-weight: bold;
  border-radius: 5px;
}


</style>