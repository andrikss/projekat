<template>
  <div class="home">
    <div class="table">
      <div class="title"><a href="/knjige">LISTA KNJIGA</a></div>
      <div class="title"><a href="/zanrovi">LISTA ZANROVA</a></div>
      <div class="title"><a href="/dodajZahtjev">PODNESI ZAHTEV ZA AUTOR NALOG</a></div>
      <div class="title"><a href="/listaAutora">LISTA AUTORA</a></div>
    </div>

      <div class="logout-button-container">
      <Logout />
      </div>

    <button @click="toggleProfileUpdate" class="azuriraj">Ažuriraj profil </button>
    <AzurirajProfil :showProfileUpdate="showProfileUpdate" />

    <div class="pretraga"><a href="/pretragaKnjiga">PRETRAGA KNJIGA</a></div>


  </div>
</template>

<script>
import Logout from '@/components/Logout.vue'
import AzurirajProfil from "@/components/AzurirajProfil.vue";
export default {
  name: "HomeCitalac",
  components: {
    Logout,
    AzurirajProfil
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

.home {
  background-color: #ffc0cb; /* Svijetlo roze pozadina */
  padding: 20px;
  width: 25%;
  align-items: flex-start;
}

.table {

  flex-direction: column;
  width: 50%;
  margin-right: 20px;
}

.title {
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
  background-color: #3498db; /* Drecavo plava boja */
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
}
</style>