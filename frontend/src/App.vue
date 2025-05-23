<template>
  <nav v-if="$route.path !== '/login' && $route.path !== '/register' && $route.path !== '/'">
    <RouterLink to="/">Home</RouterLink>
    <RouterLink to="/about">About</RouterLink>
  </nav>
  <main>
    <RouterView />
  </main>
</template>