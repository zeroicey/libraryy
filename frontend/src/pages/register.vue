<script setup lang="ts">
import { ref } from 'vue'

const username = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const handleRegister = () => {
  if (password.value !== confirmPassword.value) {
    alert('两次输入的密码不一致！')
    return
  }
  // 注册逻辑
  console.log(username.value, email.value, password.value)
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gradient-to-br from-pink-100 to-indigo-100">
    <!-- 返回首页按钮 -->
    <a href="/" class="fixed top-6 left-6 z-50 bg-white/80 hover:bg-pink-100 text-pink-500 font-bold px-4 py-2 rounded-full shadow transition-all border border-pink-200 flex items-center gap-1">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
      返回首页
    </a>
    <div class="bg-white shadow-xl rounded-3xl px-10 py-12 w-full max-w-md relative">
      <!-- Logo & Title -->
      <div class="flex flex-col items-center mb-8">
        <div class="w-14 h-14 rounded-full bg-pink-100 flex items-center justify-center mb-3 shadow">
          <svg class="w-8 h-8 text-pink-400" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20M4 19.5V6.5A2.5 2.5 0 0 1 6.5 4h11A2.5 2.5 0 0 1 20 6.5v10.5M4 19.5h16" />
          </svg>
        </div>
        <h1 class="text-3xl font-extrabold text-pink-500 tracking-wide mb-1">悦读图书馆</h1>
        <h2 class="text-lg text-pink-400 font-medium">注册新账号，开启美好阅读人生！</h2>
      </div>
      <!-- Register Form -->
      <form @submit.prevent="handleRegister" class="flex flex-col gap-6">
        <div>
          <label class="block mb-2 text-gray-700 font-semibold" for="username">用户名</label>
          <input
            id="username"
            v-model="username"
            type="text"
            required
            placeholder="请输入用户名"
            minlength="3"
            class="pl-4 pr-4 py-3 w-full rounded-xl border border-gray-200 focus:border-pink-400 focus:ring-2 focus:ring-pink-100 transition bg-gray-50 outline-none text-gray-700 placeholder-gray-400 shadow-sm"
          />
        </div>
        <div>
          <label class="block mb-2 text-gray-700 font-semibold" for="email">邮箱</label>
          <input
            id="email"
            v-model="email"
            type="email"
            required
            placeholder="请输入邮箱"
            class="pl-4 pr-4 py-3 w-full rounded-xl border border-gray-200 focus:border-pink-400 focus:ring-2 focus:ring-pink-100 transition bg-gray-50 outline-none text-gray-700 placeholder-gray-400 shadow-sm"
          />
        </div>
        <div>
          <label class="block mb-2 text-gray-700 font-semibold" for="password">密码</label>
          <input
            id="password"
            v-model="password"
            type="password"
            required
            placeholder="请输入密码"
            minlength="8"
            pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
            title="不少于8位，包含数字、小写字母、大写字母"
            class="pl-4 pr-4 py-3 w-full rounded-xl border border-gray-200 focus:border-pink-400 focus:ring-2 focus:ring-pink-100 transition bg-gray-50 outline-none text-gray-700 placeholder-gray-400 shadow-sm"
          />
          <p class="text-xs text-gray-400 mt-1 pl-1">
            不少于8位，包含数字、小写字母、大写字母
          </p>
        </div>
        <div>
          <label class="block mb-2 text-gray-700 font-semibold" for="confirmPassword">确认密码</label>
          <input
            id="confirmPassword"
            v-model="confirmPassword"
            type="password"
            required
            placeholder="请再次输入密码"
            minlength="8"
            class="pl-4 pr-4 py-3 w-full rounded-xl border border-gray-200 focus:border-pink-400 focus:ring-2 focus:ring-pink-100 transition bg-gray-50 outline-none text-gray-700 placeholder-gray-400 shadow-sm"
          />
        </div>
        <button
          class="w-full py-3 mt-2 text-lg font-bold rounded-xl bg-pink-400 text-white shadow-md hover:bg-pink-500 transition active:scale-95"
        >
          注册
        </button>
      </form>
      <!-- Login Link -->
      <div class="mt-8 text-center">
        <span class="text-gray-500 text-sm">已有账号？</span>
        <a href="/login" class="text-pink-500 hover:underline font-medium ml-1 transition-colors">去登录</a>
      </div>
    </div>
  </div>
</template>