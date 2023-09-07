<template>

  <div class = "user-info-container">
    <JedanKorisnik/>
  </div>

  <div class="kuca">
    <div class="tabb">
      <div class="tabbb-item"><a href="/knjige">LISTA KNJIGA</a></div>
      <div class="separator">|</div>
      <div class="tabbb-item"><a href="/zanrovi">LISTA ŽANROVA</a></div>
      <div class="separator">|</div>
      <div class="tabbb-item"><a href="/recenzije">LISTA RECENZIJA</a></div>
      <div class="separator">|</div>
      <div class="tabbb-item"><a href="/korisnici">LISTA KORISNIKA</a></div>
      <div class="separator">|</div>
      <div class="tabbb-item"><a href="/listaAutora">LISTA AUTORA</a></div>
      </div>
  </div>

  <div class="pretragggg"><a href="/pretragaKnjiga">PRETRAGA KNJIGA</a></div>

  <div class="azurirajjj">
    <button @click="toggleProfileUpdate" class="azurirajjjb">Ažuriraj profil </button>
    <AzurirajProfilAutora :showProfileUpdate="showProfileUpdate" />
  </div>

  <div class="logout-button-container">
    <Logout />
  </div>

  <div class="dodaj-knjigu">
    <DodajNovuKnjigu />
  </div>

</template>

<script>
import Logout from '@/components/Logout.vue'
import JedanKorisnik from "@/components/JedanKorisnik.vue";
import DodajNovuKnjigu from "@/components/DodajNovuKnjigu.vue";
import AzurirajProfilAutora from "@/components/AzurirajProfil.vue";
export default {
  name: "HomeAutor",
  components: {
    Logout,
    JedanKorisnik,
    AzurirajProfilAutora,
    DodajNovuKnjigu
  },
  data() {
    return {
      showProfileUpdate: false
    };
  },
  methods: {
    toggleProfileUpdate() {
      console.log("Toggle Profile Update method called");
      this.showProfileUpdate = !this.showProfileUpdate;
    }
  }
}
</script>

<style scoped>

.kuca {
  background-color: #d32f2f;
  padding: 20px;
  width: 530px;
  height: 34px;
  align-items: flex-start;
  bottom: 0;
  position: fixed;
  border-top-left-radius: 30px;
  border-top-right-radius: 30px;
  left: 0;
  color: white;
  font-weight: bold;
}

.tabb {
  position: absolute;
  display: flex;
  align-items: center;
  padding: 10px 0;
  bottom:0;
  left: 0px;
}

/* Stilizacija navigacionih linkova */
.tabbb-item a {
  text-decoration: none; /* Uklanja podvučenost linkova */
  color: white; /* Boja teksta */
  font-weight: bold; /* Podebljan tekst */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaje sjenku tekstu */
  transition: color 0.3s, border-bottom-color 0.3s; /* Dodaje tranziciju za boju i donju granicu teksta */
}

.tabbb-item a:hover {
  color: lightcoral; /* Boja teksta na hover-u */
  border-bottom: 2px solid #555; /* Donja granica na hover-u */
}

.logout-button-container {
  position: page; /* Postavlja "Logout" u apsolutni položaj unutar roditeljskog kontejnera */
  bottom: 0; /* Pomjeranje na dno */
  right: 0; /* Pomjeranje na desno */
  padding: 50px;
  background-color: transparent;
}


.pretragggg {
  position: fixed;
  top: 5px; /* Podešava udaljenost od vrha ekrana */
  right: 3px; /* Podešava udaljenost od desne ivice ekrana */
  padding: 20px 20px; /* Povećava unutrašnji prostor */
  background-color: #B8860B; /* Tamno žuta boja */
  color: white;
  font-weight: bold;
  font-size: 16px; /* Povećava veličinu fonta */
  font-weight: bold; /* Podebljan tekst */
  border: none; /* Uklanja okvir dugmeta */
  cursor: pointer; /* Postavite prilagođeni kursor kao lupa na hover-u preko dugmeta */
  border-radius: 30px;
  transition: color 0.3s, border-bottom-color 0.3s; /* Dodaje tranziciju za boju i donju granicu teksta */
}


.pretragggg:hover {
  text-decoration: underline; /* Podvlači tekst na hover-u */
  color: #333; /* Tamno siva boja teksta na hover-u */
}

.pretragggg a {
  color: white;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaje sjenku tekstu */
  text-decoration: none; /* Uklanja podvučenost linkova */
}

.azurirajjj {
  position: fixed;
  bottom: calc(15% - 25px); /* 50% s lijeve strane ekrana, -25px za polovicu visine gumba */
  left: 4px;
  display: inline-block;
  padding: 10px;
  border-radius: 5px;
  background-color: transparent;
  color: orangered;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
}

.azurirajjjb {
  position: fixed;
  bottom: calc(15% - 25px); /* 50% s lijeve strane ekrana, -25px za polovicu visine gumba */
  left: 4px;
  display: inline-block;
  padding: 10px;
  border-radius: 5px;
  background-color: whitesmoke;
  color: orangered;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
}


.azurirajjjb:hover {
  background-color: orangered; /* Promijenite background boju za hover */
  color: white; /* Boja teksta za hover */
}

.user-info-container {
  display: flex; /* Koristi fleksibilno poravnanje */
  align-content: flex-start;
  justify-content: center; /* Poravnavanje sadržaja horizontalno */
  align-items: center; /* Poravnavanje sadržaja vertikalno */
  margin-top: 0px;
  margin-left: 280px;
  width: 950px;
  padding: 15px;
}


.separator {
  font-weight: bold;
  margin: 0 5px; /* Add spacing around the separator */
}

.dodaj-knjigu {
  background-color: rgba(178, 34, 34, 0.5);
  position: fixed;
  bottom: 2px;
  right: 0px;
  height: 55vh;
  width: 280px;
  padding: 20px;
  display: flex;
  border-radius: 20px;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Dodaje sjenku tekstu */
  flex-direction: column;
  justify-content: space-between;
  font-weight: bold;
}

</style>