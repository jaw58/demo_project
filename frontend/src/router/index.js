import { createWebHistory, createRouter } from "vue-router";
import StudentLogin from "../components/StudentLogin.vue";

const routes = [
  {
    path: "/",
    alias: "/login",
    name: "login",
    component: StudentLogin
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;