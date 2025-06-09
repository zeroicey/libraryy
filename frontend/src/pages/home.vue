<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50">
    <!-- 主要内容 -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <!-- 欢迎区域 -->
      <section class="text-center mb-16">
        <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-indigo-100 mb-6">
          <svg class="w-12 h-12 text-indigo-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 1 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25" />
          </svg>
        </div>
        <h1 class="text-5xl font-bold text-gray-900 mb-4">Libraryy 阅读图书馆</h1>
        <p class="text-xl text-gray-600 max-w-3xl mx-auto leading-relaxed">
          现代化的数字图书馆管理系统，为读者提供便捷的在线阅读体验和智能化的图书管理服务
        </p>
      </section>

      <!-- 技术栈展示 -->
      <section class="mb-16">
        <h2 class="text-3xl font-bold text-center text-gray-900 mb-8">技术架构</h2>
        <div class="grid md:grid-cols-2 gap-8">
          <div class="bg-white rounded-2xl p-8 shadow-lg hover:shadow-xl transition-shadow">
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 rounded-full bg-green-100 flex items-center justify-center mr-4">
                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
              </div>
              <h3 class="text-xl font-semibold text-gray-900">前端技术</h3>
            </div>
            <div class="space-y-2">
              <div class="flex items-center">
                <span class="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                <span class="text-gray-700">Vue.js - 现代化前端框架</span>
              </div>
              <div class="flex items-center">
                <span class="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                <span class="text-gray-700">TypeScript - 类型安全</span>
              </div>
              <div class="flex items-center">
                 <span class="w-2 h-2 bg-purple-500 rounded-full mr-3"></span>
                 <span class="text-gray-700">Tailwind CSS - 响应式设计</span>
               </div>
               <div class="flex items-center">
                 <span class="w-2 h-2 bg-indigo-500 rounded-full mr-3"></span>
                 <span class="text-gray-700">Pinia - 状态管理</span>
               </div>
            </div>
          </div>
          <div class="bg-white rounded-2xl p-8 shadow-lg hover:shadow-xl transition-shadow">
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 rounded-full bg-orange-100 flex items-center justify-center mr-4">
                <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M5 12a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v4a2 2 0 01-2 2M5 12a2 2 0 00-2 2v4a2 2 0 002 2h14a2 2 0 002-2v-4a2 2 0 00-2-2" />
                </svg>
              </div>
              <h3 class="text-xl font-semibold text-gray-900">后端技术</h3>
            </div>
            <div class="space-y-2">
              <div class="flex items-center">
                <span class="w-2 h-2 bg-red-500 rounded-full mr-3"></span>
                <span class="text-gray-700">Java - 企业级开发语言</span>
              </div>
              <div class="flex items-center">
                <span class="w-2 h-2 bg-yellow-500 rounded-full mr-3"></span>
                <span class="text-gray-700">Servlet - Web服务框架</span>
              </div>
              <div class="flex items-center">
                <span class="w-2 h-2 bg-indigo-500 rounded-full mr-3"></span>
                <span class="text-gray-700">JDBC - 数据库连接</span>
              </div>
              <div class="flex items-center">
                <span class="w-2 h-2 bg-pink-500 rounded-full mr-3"></span>
                <span class="text-gray-700">FastJSON2 & JWT - 数据处理与认证</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 主要功能 -->
      <section class="mb-16">
        <h2 class="text-3xl font-bold text-center text-gray-900 mb-12">核心功能</h2>
        <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div class="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-all hover:-translate-y-1">
            <div class="w-14 h-14 rounded-full bg-blue-100 flex items-center justify-center mb-4 mx-auto">
              <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 17.25v3.375c0 .621-.504 1.125-1.125 1.125h-9.75a1.125 1.125 0 01-1.125-1.125V7.875c0-.621.504-1.125 1.125-1.125H6.75a9.06 9.06 0 011.5.124m7.5 10.376h3.375c.621 0 1.125-.504 1.125-1.125V11.25c0-4.46-3.243-8.161-7.5-8.876a9.06 9.06 0 00-1.5-.124H9.375c-.621 0-1.125.504-1.125 1.125v3.5m7.5 10.375H9.375a1.125 1.125 0 01-1.125-1.125v-9.25m12 6.625v-1.875a3.375 3.375 0 00-3.375-3.375h-1.5a1.125 1.125 0 01-1.125-1.125v-1.5a3.375 3.375 0 00-3.375-3.375H9.75" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900 text-center mb-2">在线电子书预览</h3>
            <p class="text-gray-600 text-center text-sm">支持多种格式的电子书在线阅读，提供流畅的阅读体验</p>
          </div>
          <div class="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-all hover:-translate-y-1">
            <div class="w-14 h-14 rounded-full bg-green-100 flex items-center justify-center mb-4 mx-auto">
              <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.678 48.678 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.017.22-.032.441-.046.662M19.5 12l3-3m-3 3l-3-3m-12 3c0 1.232.046 2.453.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7c.017-.22.032-.441.046-.662M4.5 12l3 3m-3-3l-3 3" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900 text-center mb-2">书籍借阅归还</h3>
            <p class="text-gray-600 text-center text-sm">智能化的借阅管理系统，支持在线借阅和归还操作</p>
          </div>
          <div class="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-all hover:-translate-y-1">
            <div class="w-14 h-14 rounded-full bg-purple-100 flex items-center justify-center mb-4 mx-auto">
              <svg class="w-8 h-8 text-purple-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900 text-center mb-2">管理员录入书籍</h3>
            <p class="text-gray-600 text-center text-sm">便捷的图书信息录入和管理功能，支持批量操作</p>
          </div>
          <div class="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-all hover:-translate-y-1">
            <div class="w-14 h-14 rounded-full bg-indigo-100 flex items-center justify-center mb-4 mx-auto">
              <svg class="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900 text-center mb-2">用户借阅管理</h3>
            <p class="text-gray-600 text-center text-sm">个人借阅记录查询，到期提醒和续借功能</p>
          </div>
        </div>
      </section>

      <!-- 快速开始 -->
      <section class="text-center">
        <div class="bg-gradient-to-r from-indigo-500 to-purple-600 rounded-2xl p-12 text-white">
          <h2 class="text-3xl font-bold mb-4">开始您的阅读之旅</h2>
          <p class="text-xl mb-8 opacity-90">立即注册，探索丰富的数字图书资源</p>
          <div class="flex justify-center space-x-4">
            <router-link v-if="!isLogin" to="/register" class="bg-white text-indigo-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition">
              立即注册
            </router-link>
            <router-link v-if="!isLogin" to="/login" class="border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-indigo-600 transition">
              用户登录
            </router-link>
            <div v-else class="text-xl">
              欢迎回来，{{ username }}！开始探索图书世界吧 📚
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- 页脚 -->
    <footer class="bg-gray-900 text-white py-12 mt-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center">
          <div class="flex items-center justify-center space-x-3 mb-4">
            <div class="w-8 h-8 rounded-full bg-indigo-600 flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20M4 19.5V6.5A2.5 2.5 0 0 1 6.5 4h11A2.5 2.5 0 0 1 20 6.5v10.5M4 19.5h16" />
              </svg>
            </div>
            <span class="text-xl font-bold">Libraryy 阅读图书馆</span>
          </div>
          <p class="text-gray-400 mb-4">现代化数字图书馆管理系统</p>
          <p class="text-gray-500 text-sm">
            © 2024 Libraryy. 基于 Vue.js + Java 构建
          </p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useUserStore } from '../store/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const { token, username, isLogin } = storeToRefs(userStore)

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
</script>