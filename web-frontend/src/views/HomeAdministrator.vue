<template>

  <div class = "user-info-container">
    <JedanKorisnik/>
  </div>

  <div class="home">
    <div class="table">
      <div class="table-item"><a href="/knjige">LISTA KNJIGA</a></div>
      <div class="separator">|</div>
      <div class="table-item"><a href="/zanrovi">LISTA ZANROVA</a></div>
      <div class="separator">|</div>
      <div class="table-item"><a href="/listaAutora">LISTA AUTORA</a></div>
      <div class="separator">|</div>
      <div class="table-item"><a href="/korisnici">LISTA KORISNIKA</a></div>
      <div class="separator">|</div>
      <div class="table-item"><a href="/zahtjevi"> LISTA ZAHTJEVA</a></div>
    </div>
  </div>

  <div class="pretraga"><a href="/pretragaKnjiga">PRETRAGA KNJIGA</a></div>

  <div class="azuriraj-container">
    <button @click="toggleProfileUpdate" class="azuriraj">Ažuriraj profil </button>
    <AzurirajProfil :showProfileUpdate="showProfileUpdate" />
  </div>

  <div class="logout-button-container">
    <Logout />
  </div>

  <div class="dodaj-knjigu">
    <DodajNovuKnjigu />
  </div>

  <div class="napravi-autora">
    <button @click="toggleCreateAuthor" class="napravi-autora-button">Napravi autora</button>
    <NapraviAutora :showCreateAuthor="showCreateAuthor" />
  </div>

</template>

<script>
import Logout from '@/components/Logout.vue'
import JedanKorisnik from "@/components/JedanKorisnik.vue";
import DodajNovuKnjigu from "@/components/DodajNovuKnjigu.vue";
import AzurirajProfil from "@/components/AzurirajProfil.vue";
import NapraviAutora from "@/components/NapraviAutora.vue";
export default {
  name: "HomeAdministrator",
  components: {
    Logout,
    JedanKorisnik,
    AzurirajProfil,
    DodajNovuKnjigu,
    NapraviAutora
  },
  data() {
    return {
      showProfileUpdate: false,
      showCreateAuthor: false
    };
  },
  methods: {
    toggleProfileUpdate() {
      this.showProfileUpdate = !this.showProfileUpdate;
    },
    toggleCreateAuthor() {
      this.showCreateAuthor = !this.showCreateAuthor;
    }
  }
}
</script>

<style scoped>

.home {
  background-color: #ffc0cb; /* Svijetlo roze pozadina */
  padding: 20px;
  width: 38%;
  height: 34px;
  align-items: flex-start;
  bottom: 0;
  position: fixed;
  left: 29%;
}

.table {
  position: absolute;
  display: flex;
  align-items: center;
  padding: 10px 0;
  bottom:0;
  left: 40px;
}

.table-item {
  font-weight: bold;
  color: #6b0f9c; /* Tamno ljubičasta boja */
  margin-bottom: 10px;
}

.logout-button-container {
  position: absolute; /* Postavlja "Logout" u apsolutni položaj unutar roditeljskog kontejnera */
  bottom: 0; /* Pomjeranje na dno */
  right: 0; /* Pomjeranje na desno */
  padding: 50px;
  background-color: #ff6666;
}

.pretraga {
  position: fixed;
  top: 30px; /* Podešava udaljenost od vrha ekrana */
  right: 0; /* Podešava udaljenost od desne ivice ekrana */
  padding: 30px 40px; /* Povećava unutrašnji prostor */
  background-color: darkseagreen; /* Drecavo plava boja */
  color: white; /* Bijela boja teksta */
  font-size: 16px; /* Povećava veličinu fonta */
  font-weight: bold; /* Podebljan tekst */
  border: none; /* Uklanja okvir dugmeta */
  cursor: pointer; /* Pokazivač prilikom hover-a preko dugmeta */
  z-index: 9999; /* Postavlja z-index kako bi bio iznad drugih elemenata */
  transform: translateX(-50%); /* Pomeranje elementa za polovinu širine kako bi bio centriran */
}

.azuriraj {
  background-color: plum; /* Drecavo plava boja */
  color: deeppink; /* Bijela boja teksta */
  font-size: 16px; /* Povećava veličinu fonta */
  font-weight: bold; /* Podebljan tekst */
  padding: 10px 20px; /* Povećava unutrašnji prostor dugmeta */
  border: none; /* Uklanja okvir dugmeta */
  cursor: pointer; /* Pokazivač prilikom hover-a preko dugmeta */
  margin: 0;
  height: 50px;
}

.azuriraj-container {
  position: absolute;
  top: 0;
  left: 4px;
  display: inline-block;
  padding: 10px;
  border-radius: 5px;
}

.azuriraj:hover {
  background-color: plum; /* Promijenite background boju za hover */
  color: white; /* Boja teksta za hover */
}

.user-info-container {
  display: flex; /* Koristi fleksibilno poravnanje */
  justify-content: center; /* Poravnavanje sadržaja horizontalno */
  align-items: center; /* Poravnavanje sadržaja vertikalno */
  height: 15vh;
  margin-top: 40px;
  width: 100%;
}

.table-item {
  padding: 0 10px; /* Add horizontal spacing between table items */
}

.separator {
  font-weight: bold;
  margin: 0 5px; /* Add spacing around the separator */
}

.dodaj-knjigu {
  background-color: greenyellow;
  position: fixed;
  bottom: 197px;
  right: 0px;
  height: 40vh;
  width: 280px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  font-weight: bold;

}

.dodaj-knjigu button {
  background-color: white;
  color: indianred;
  font-weight: bold;
  padding: 10px;
  border: none;
  cursor: pointer;
}

.napravi-autora {
  position: absolute;
  bottom: 10px;
  left: 2px;
  background-color: darkorange;
  color: white;
  font-weight: bold;
  width: 300px;
  border-radius: 5px;
  padding: 10px;
}

.napravi-autora-button {
  background-color: darkorange; /* Background boja dugmeta */
  color: white; /* Boja teksta */
  font-size: 18px; /* Veličina fonta */
  font-weight: bold; /* Podebljano slovo */
  padding: 15px 30px; /* Unutrašnji prostor dugmeta */
  border: none; /* Uklanja okvir dugmeta */
  cursor: pointer; /* Pokazivač prilikom hover-a preko dugmeta */
  border-radius: 5px; /* Zaobljeni rubovi */
}

.napravi-autora-button:hover {
  background-color: orange; /* Promijenite background boju za hover */
  color: white; /* Boja teksta za hover */
}
</style>