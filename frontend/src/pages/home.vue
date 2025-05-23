<template>
  <div class="min-h-screen bg-gradient-to-br from-indigo-100 to-pink-100">
    <!-- 顶部导航栏 -->
    <header class="flex items-center justify-between px-10 py-6 bg-white/80 shadow-md sticky top-0 z-40">
      <div class="flex items-center gap-3">
        <img src="/vite.svg" class="h-10 w-10" alt="logo" />
        <span class="text-2xl font-extrabold text-indigo-600 tracking-wide">悦读图书馆（libraryy）</span>
      </div>
      <nav class="flex gap-8 text-lg font-medium">
        <a href="#explore" class="text-indigo-500 hover:text-pink-400 transition">查阅书籍</a>
        <a href="/login" class="text-indigo-500 hover:text-pink-400 transition">登录</a>
        <a href="/register" class="text-indigo-500 hover:text-pink-400 transition">注册</a>
      </nav>
    </header>

    <!-- 英雄区 -->
    <section class="flex flex-col md:flex-row items-center justify-between px-10 py-16 max-w-7xl mx-auto gap-10">
      <div class="flex-1">
        <h1 class="text-4xl md:text-5xl font-extrabold text-indigo-700 mb-6 leading-tight">
          悦读图书馆（libraryy）<br />
          <span class="text-pink-400">开启智慧阅读新生活</span>
        </h1>
        <p class="text-lg text-gray-600 mb-8">本项目为演示型图书馆平台，支持在线查阅等功能，所有数据均为模拟展示。</p>
        <div class="flex gap-4">
          <a href="#explore" class="px-6 py-3 bg-indigo-500 text-white rounded-xl font-bold shadow hover:bg-indigo-600 transition">立即查阅</a>
        </div>
      </div>
      <div class="flex-1 flex justify-center">
        <img src="https://img1.imgtp.com/2023/07/21/0QvQnQwA.png" alt="library hero" class="rounded-3xl shadow-2xl w-full max-w-md" />
      </div>
    </section>

    <!-- 功能介绍区 -->
    <section class="py-12 bg-white/80">
      <div class="max-w-6xl mx-auto px-6 grid grid-cols-1 md:grid-cols-3 gap-10">
        <div class="flex flex-col items-center text-center p-6 bg-white rounded-2xl shadow hover:shadow-lg transition">
          <div class="bg-indigo-100 text-indigo-500 rounded-full p-4 mb-4">
            <svg class="w-8 h-8" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M12 20h9"/><path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m0 0H3"/></svg>
          </div>
          <h3 class="text-xl font-bold mb-2">在线查阅</h3>
          <p class="text-gray-500">浏览丰富的图书资源，支持分类、搜索、详情预览，随时随地开启阅读。</p>
        </div>
        <div class="flex flex-col items-center text-center p-6 bg-white rounded-2xl shadow hover:shadow-lg transition">
          <div class="bg-pink-100 text-pink-400 rounded-full p-4 mb-4">
            <svg class="w-8 h-8" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
          </div>
          <h3 class="text-xl font-bold mb-2">便捷借书</h3>
          <p class="text-gray-500">一键申请借阅，模拟借书流程，体验真实的图书馆服务。</p>
        </div>
        <div class="flex flex-col items-center text-center p-6 bg-white rounded-2xl shadow hover:shadow-lg transition">
          <div class="bg-indigo-100 text-indigo-500 rounded-full p-4 mb-4">
            <svg class="w-8 h-8" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 17v-2a4 4 0 0 1 8 0v2"/><path stroke-linecap="round" stroke-linejoin="round" d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-7"/></svg>
          </div>
          <h3 class="text-xl font-bold mb-2">轻松还书</h3>
          <p class="text-gray-500">模拟还书操作，体验完整的借还流程，养成良好阅读习惯。</p>
        </div>
      </div>
    </section>

    <!-- 书籍查阅区 -->
    <section id="explore" class="py-16 bg-white/70">
      <div class="max-w-7xl mx-auto px-6">
        <h2 class="text-3xl font-bold text-indigo-700 mb-8">热门在线书籍</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
          <div v-for="book in books" :key="book.id" class="bg-white rounded-2xl shadow-lg p-6 flex flex-col">
            <img :src="book.cover" :alt="book.title" class="h-48 w-full object-cover rounded-xl mb-4" />
            <h3 class="text-xl font-semibold text-indigo-600 mb-2">{{ book.title }}</h3>
            <p class="text-gray-500 mb-4 line-clamp-2">{{ book.desc }}</p>
            <div class="flex gap-2 mt-auto">
              <button class="px-4 py-2 bg-indigo-100 text-indigo-600 rounded-lg font-bold hover:bg-indigo-200 transition">在线阅读</button>
              <button class="px-4 py-2 bg-pink-100 text-pink-500 rounded-lg font-bold hover:bg-pink-200 transition">借阅</button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 数据展示区 -->
    <section class="py-16 bg-gradient-to-r from-indigo-50 to-pink-50">
      <div class="max-w-6xl mx-auto px-6 grid grid-cols-1 md:grid-cols-3 gap-10">
        <div class="bg-white rounded-2xl shadow p-8 flex flex-col items-center">
          <div class="text-4xl font-extrabold text-indigo-500 mb-2">{{ books.length }}</div>
          <div class="text-gray-600">馆藏书籍（演示）</div>
        </div>
        <div class="bg-white rounded-2xl shadow p-8 flex flex-col items-center">
          <div class="text-4xl font-extrabold text-pink-400 mb-2">128</div>
          <div class="text-gray-600">累计借阅（演示）</div>
        </div>
        <div class="bg-white rounded-2xl shadow p-8 flex flex-col items-center">
          <div class="text-4xl font-extrabold text-indigo-500 mb-2">120</div>
          <div class="text-gray-600">累计还书（演示）</div>
        </div>
      </div>
    </section>

    <!-- 底部 -->
    <footer class="py-8 text-center text-gray-400 text-sm bg-white/80 mt-10">
      © 2025 悦读图书馆（libraryy） | 在线查阅 · 借书 · 还书
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const books = ref([
  {
    id: 1,
    title: '活着',
    cover: 'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=400&q=80',
    desc: '余华经典小说，讲述普通人在苦难中坚韧生活的故事。',
  },
  {
    id: 2,
    title: '三体',
    cover: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80',
    desc: '刘慈欣科幻巨著，开启中国科幻新纪元。',
  },
  {
    id: 3,
    title: '百年孤独',
    cover: 'https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=400&q=80',
    desc: '马尔克斯魔幻现实主义巅峰之作。',
  },
  {
    id: 4,
    title: '小王子',
    cover: 'https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=400&q=80',
    desc: '一部温暖人心的童话，适合所有年龄段的读者。',
  },
])
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-clamp: 2;
}
</style>