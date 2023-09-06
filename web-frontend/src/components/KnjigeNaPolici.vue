<template>
  <div>
    <h2>Knjige na polici : {{ nazivPolice }} </h2>
    <ul>
      <li v-for="knjiga in knjige" :key="knjiga.id">
        {{ knjiga.naslov }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "KnjigeNaPolici",
  props: {
    nazivPolice: String,
    knjige: Array,
  },
}
</script>

<style scoped>

</style>