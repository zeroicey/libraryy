import { create } from "zustand";
import { persist } from "zustand/middleware";

interface UserState {
  id: number;
  username: string;
  email: string;
  avatar: string;
  token: string;
  current_book: string;
  setCurrentBook: (book: string) => void;
  setUser: (User: UserInfo) => void;
  setId: (id: number) => void;
  setToken: (token: string) => void;
  setUsername: (username: string) => void;
  setEmail: (email: string) => void;
  logout: () => void;
}
export const useUserStore = create<UserState>()(
  persist(
    (set) => ({
      id: -1,
      username: "guide",
      email: "guide@lifetrack.cc",
      avatar: "https://api.dicebear.com/7.x/pixel-art/svg?seed=guide",
      token: "",
      current_book: "",
      setUser: (User: UserInfo) =>
        set(() => ({
          id: User.id,
          username: User.username,
          email: User.email,
        })),
      setCurrentBook: (book: string) => set(() => ({ current_book: book })),
      setToken: (token: string) => set(() => ({ token })),
      setId: (id: number) => set(() => ({ id })),
      setUsername: (username: string) =>
        set(() => ({
          username,
          avatar: `https://api.dicebear.com/7.x/pixel-art/svg?seed=${username}`,
        })),
      setEmail: (email: string) => set(() => ({ email })),
      logout: () =>
        set(() => ({
          token: "",
          id: -1,
          username: "guide",
          email: "guide@libraryy.cc",
        })),
    }),
    {
      name: "user-storage", // 存储在 localStorage 中的键名
    }
  )
);
