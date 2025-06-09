import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => {
    return {
      id: -1,
      token: "",
      username: "",
      email: "",
    };
  },
  getters: {
    getToken(): string {
      return this.token;
    },

    getUsername(): string {
      return this.username;
    },

    getEmail(): string {
      return this.email;
    },

    getId(): number {
      return this.id;
    },

    isLogin(): boolean {
      return this.token !== "";
    },
  },
  actions: {
    setToken(token: string) {
      this.token = token;
    },

    setUsername(username: string) {
      this.username = username;
    },

    setEmail(email: string) {
      this.email = email;
    },

    setUser(id: number, username: string, email: string) {
      this.id = id;
      this.username = username;
      this.email = email;
    },

    clearUser() {
      this.id = -1;
      this.username = "";
      this.email = "";
    },

    logout() {
      this.clearUser();
      this.token = "";
    },
  },
  persist: true,
});
