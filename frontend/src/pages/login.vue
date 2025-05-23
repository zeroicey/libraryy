<script setup lang="ts">
import { ref } from 'vue'

const nameOrEmail = ref('')
const password = ref('')

const handleSubmit = () => {
    console.log(nameOrEmail.value, password.value)
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 to-indigo-200">
    <!-- 返回首页按钮 -->
    <a href="/" class="fixed top-6 left-6 z-50 bg-white/80 hover:bg-indigo-100 text-indigo-500 font-bold px-4 py-2 rounded-full shadow transition-all border border-indigo-200 flex items-center gap-1">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
      返回首页
    </a>
    <div class="bg-white shadow-xl rounded-3xl px-10 py-12 w-full max-w-md relative">
      <!-- Logo & Title -->
      <div class="flex flex-col items-center mb-8">
        <div class="w-14 h-14 rounded-full bg-indigo-100 flex items-center justify-center mb-3 shadow">
          <svg class="w-8 h-8 text-indigo-500" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round"
              d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20M4 19.5V6.5A2.5 2.5 0 0 1 6.5 4h11A2.5 2.5 0 0 1 20 6.5v10.5M4 19.5h16" />
          </svg>
        </div>
        <h1 class="text-3xl font-extrabold text-indigo-700 tracking-wide mb-1">悦读图书馆</h1>
        <h2 class="text-lg text-indigo-500 font-medium">欢迎回来，开启你的阅读之旅！</h2>
      </div>
      <!-- Login Form -->
      <form @submit.prevent="handleSubmit" class="flex flex-col gap-6">
        <div>
          <label class="block mb-2 text-gray-700 font-semibold" for="nameOrEmail">用户名或邮箱</label>
          <div class="relative">
            <span class="absolute left-3 top-1/2 -translate-y-1/2 text-indigo-400">
              <svg class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round"
                  d="M16 12a4 4 0 1 0-8 0 4 4 0 0 0 8 0zm2 6a6 6 0 1 0-12 0h12z" />
              </svg>
            </span>
            <input
              id="nameOrEmail"
              v-model="nameOrEmail"
              type="text"
              required
              placeholder="请输入用户名或邮箱"
              minlength="8"
              pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
              title="不少于8位，包含数字、小写字母、大写字母"
              class="pl-10 pr-4 py-3 w-full rounded-xl border border-gray-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition bg-gray-50 outline-none text-gray-700 placeholder-gray-400 shadow-sm"
            />
          </div>
          <p class="text-xs text-gray-400 mt-1 pl-1">
            不少于8位，包含数字、小写字母、大写字母
          </p>
        </div>
        <div>
          <label class="block mb-2 text-gray-700 font-semibold" for="password">密码</label>
          <div class="relative">
            <span class="absolute left-3 top-1/2 -translate-y-1/2 text-indigo-400">
              <svg class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round"
                  d="M12 15v2m-6 4h12a2 2 0 0 0 2-2v-6a2 2 0 0 0-2-2H6a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2zm6-10V7a4 4 0 1 0-8 0v2" />
              </svg>
            </span>
            <input
              id="password"
              v-model="password"
              type="password"
              required
              placeholder="请输入密码"
              minlength="8"
              pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
              title="不少于8位，包含数字、小写字母、大写字母"
              class="pl-10 pr-4 py-3 w-full rounded-xl border border-gray-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition bg-gray-50 outline-none text-gray-700 placeholder-gray-400 shadow-sm"
            />
          </div>
          <p class="text-xs text-gray-400 mt-1 pl-1">
            不少于8位，包含数字、小写字母、大写字母
          </p>
        </div>
        <button
          class="w-full py-3 mt-2 text-lg font-bold rounded-xl bg-indigo-500 text-white shadow-md hover:bg-indigo-600 transition active:scale-95"
        >
          登录
        </button>
      </form>
      <!-- Register Link -->
      <div class="mt-8 text-center">
        <span class="text-gray-500 text-sm">还没有账号？</span>
        <a href="/register" class="text-indigo-600 hover:underline font-medium ml-1 transition-colors">去注册</a>
      </div>
    </div>
  </div>
</template>