<template>
  <v-app>
    <v-app-bar app dark color = "orange darken-1">
      <div class="d-flex align-center mr-2">
        Jumia
      </div>

      <v-btn to="/customers" text>
        Customers
      </v-btn>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  components: {
  },

  data: () => ({
    //
  }),
};
</script>
