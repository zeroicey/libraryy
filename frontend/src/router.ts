import { createMemoryHistory, createRouter } from "vue-router";
import Home from "./pages/home.vue";
import Login from "./pages/login.vue";
import Register from "./pages/register.vue";

const routes = [
  { path: "/", component: Home },
  { path: "/login", component: Login },
  { path: "/register", component: Register },
];

const router = createRouter({
  history: createMemoryHistory(),
  routes,
});

export default router;
