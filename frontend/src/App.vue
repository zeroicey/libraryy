<template>
  <NavBar v-if="$route.path !== '/login' && $route.path !== '/register'" />
  <main>
    <RouterView />
  </main>
</template>

<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import NavBar from './components/NavBar.vue'
const $route = useRoute()
</script>

<style></style>