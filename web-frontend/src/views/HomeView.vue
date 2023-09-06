<template>
  <div class="home">
    <div class="home">
      <div class="header">
        <h1 class="header-title">Dobrodošli u Biblioteku</h1>
      </div>

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
      <span class="separator">|</span>
      <a href="/listaAutora">LISTA AUTORA</a>

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
  margin: 0;
  padding: 0;
  background-image: url('@/assets/slike/pozadinaa.jpg');
  background-size: cover;
  background-position: center;
  text-align: center;
  padding: 20px;
  background-size: 100% 100%; /* Da se slika potpuno popuni cijeli ekran */
  background-repeat: no-repeat; /* Onemogućava ponavljanje pozadinske slike */
  background-color: rgba(0, 0, 0, 0); /* Transparentna pozadina */}

.navbar {
  margin-bottom: 20px;
  background-color: #fff; /* Postavite belu pozadinu za listu sa nav linkovima */
  padding: 10px; /* Dodajte malo prostora između linkova i ivica */
  border: 4px solid; /* Promenite debljinu granica ovde */
  border-radius: 10px;
}

.navbar a {
  text-decoration: none;
  color: #333;
  font-weight: bold;
  margin-right: 10px;
  border-bottom: 2px solid transparent; /* Dodajte donju granicu za linkove */
  transition: border-color 0.3s; /* Dodajte animaciju prelaska mišem */
}

.navbar a:hover {
  border-color: orangered; /* Promenite boju granice prilikom prelaska mišem */
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
  border: 4px solid;
  border-radius: 10px;
  background-color: rgba(150, 150, 150, 0.6); /* Postavite svijetlo sivu pozadinu sa providnošću */}

.auth-section:nth-child(1) {
  border-color: orangered; /* Roze boja za Login */
}

.auth-section:nth-child(2) {
  border-color: orangered; /* Ljubičasta boja za Register */
}

.auth-title {
  font-size: 27px;
  margin-bottom: 10px;
  font-weight: bold;
  font-family: 'Montserrat', sans-serif;
  color: white; /* Bijela boja za naslov */
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.5); /* Dodajte sjenku tekstu */
  text-transform: uppercase;
}

.activation-request {
  position: fixed;
  bottom: 20px;
  left: 20px;
  padding: 15px;
  width: 43%;
  background-color: white; /* Transparentna pozadina */
  color: #333333; /* Tamno siva boja teksta */
  font-weight: bold;
  border-radius: 5px;
  text-align: center; /* Centralizujte tekst */
  text-transform: uppercase; /* Pretvorite tekst u velika slova */
  border: 3px solid #333; /* Tamno siva granica oko zahtjeva */
}

.activation-request:hover {
  border-color: orangered; /* Promenite boju granice prilikom prelaska mišem */
}

/* Ako želite da stilizujete linkove unutar zahtjeva za aktivaciju */
.activation-request a {
  color: #333; /* Boja linkova kao kod navigacionih linkova */
  text-decoration: underline; /* Dodajte podvučene linkove */
}

/* Ako želite da uklonite podvučenje linkova prilikom hover-a */
.activation-request a:hover {
  text-decoration: none;
  color: orangered;
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.header-title {
  font-size: 60px;
  margin-bottom: 20px;
  font-weight: bold;
  color: white; /* Bijela boja za naslov */
  text-shadow: 3px 3px 5px rgba(0, 0, 0, 0.8); /* Dodajte sjenku tekstu */
  text-transform: uppercase; /* Pretvorite tekst u velika slova */
}
</style>