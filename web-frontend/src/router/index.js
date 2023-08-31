import { createRouter, createWebHistory } from "vue-router";
import Login from "@/components/Login.vue";
import Register from "@/components/Register.vue"; // Dodajte import za Login komponentu
import HomeView from "@/views/HomeView.vue";
import KnjigeView from "@/views/KnjigeView.vue";
import KnjigaView from "@/views/KnjigaView.vue";
import PregledKnjigaView from "@/views/PregledKnjigaView.vue";
import RecenzijaView from "@/views/RecenzijaView.vue";
import ZanroviView from "@/views/ZanroviView.vue";
import KorisniciView from "@/views/KorisniciView.vue";
import DodajZahtjevView from "@/views/DodajZahtjevView.vue";
import HomeCitalac from "@/views/HomeCitalac.vue";
import AutoriView from "@/views/AutoriView.vue";
import AutorView from "@/views/AutorView.vue";
import PolicaView from "@/views/PolicaView.vue";
import HomeAutor from "@/views/HomeAutor.vue";
import HomeAdministrator from "@/views/HomeAdministrator.vue";
import DodajRecenzijuView from "@/views/DodajRecenzijuView.vue";
import JednaRecenzija from "@/views/JednaRecenzija.vue";
import KorisnikView from "@/views/KorisnikView.vue";

const routes = [
  { path: "/register", component: Register },
  { path: "/login", component: Login },
  { path: '/', name: 'home', component: HomeView },
  { path: '/knjige', name: 'knjige', component: KnjigeView },
  { path: '/knjiga', name: 'knjigaJedna', component: KnjigaView },
  { path: '/pretragaKnjiga', name: 'searchKnjiga', component: PregledKnjigaView},
  { path: '/recenzije', name: 'recenzije', component: RecenzijaView},
  { path: '/zanrovi', name: 'zanrovi', component: ZanroviView},
  { path: '/korisnici', name: 'korisnici', component: KorisniciView},
  { path: '/dodajZahtjev', name: 'dodajZahtjev', component: DodajZahtjevView},
  { path:'/homeCitalac', name:'homeCitalac', component: HomeCitalac},
  { path: '/listaAutora', name:'listaAutora', component: AutoriView},
  { path: '/autor', name: 'autor', component: AutorView},
  { path: '/polica', name: 'polica', component: PolicaView},
  { path: '/homeAutor', name: 'homeAutor', component: HomeAutor},
  { path: '/homeAdministrator', name: 'homeAdministrator', component: HomeAdministrator},
  { path: '/dodajRecenziju/knjiga', name: 'dodajRecenziju', component:  DodajRecenzijuView},
  { path: '/recenzija', name: 'recenzija', component: JednaRecenzija},
  { path: '/korisnik', name: 'korisnik', component: KorisnikView}
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
