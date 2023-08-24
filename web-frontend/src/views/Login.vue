<template>
  <div class="login-form">
    <h2>Login</h2>
    <label for="email">Email:</label>
    <input v-model="email" /><br />
    <label for="password">Password:</label>
    <input type="password" v-model="password" /><br />
    <button @click="login">Login</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    async login() {
      try {
        const response = await fetch("http://localhost:9090/api/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            emailAdresa: this.email,
            lozinka: this.password,
          }),
        });

        if (response.ok) {
          const user = await response.json();
          console.log("Logged in:", user);
          // Preusmeravanje korisnika nakon uspešnog logovanja
          this.$router.push("/home");
        } else {
          console.log("Login failed");
        }
      } catch (error) {
        console.error("Error:", error);
      }
    },
  },
};
</script>

<style scoped>
/* Stilizacija forme */
</style>
