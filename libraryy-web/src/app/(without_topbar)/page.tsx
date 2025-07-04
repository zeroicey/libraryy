"use client";

import Link from "next/link";
import Image from "next/image";
import { useState, useEffect } from "react";
import { useUserStore } from "@/store/user";
import { useRouter } from "next/navigation";

export default function HomePage() {
  const { token } = useUserStore();
  const router = useRouter();
  const [mounted, setMounted] = useState(false);
  const [currentFeature, setCurrentFeature] = useState(0);

  useEffect(() => {
    setMounted(true);
    const interval = setInterval(() => {
      setCurrentFeature((prev) => (prev + 1) % 4);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const features = [
    {
      icon: "📚",
      title: "在线阅读",
      subtitle: "电子书预览功能",
      description: "支持多种格式的电子书在线预览，提供基础的阅读体验",
      color: "from-blue-500 to-cyan-500",
      bgColor: "bg-gradient-to-br from-blue-50 to-cyan-50",
    },
    {
      icon: "⚡",
      title: "借阅管理",
      subtitle: "图书借还系统",
      description: "实现图书的借阅、归还功能，记录借阅历史和状态",
      color: "from-purple-500 to-pink-500",
      bgColor: "bg-gradient-to-br from-purple-50 to-pink-50",
    },
    {
      icon: "🎯",
      title: "后台管理",
      subtitle: "管理员功能模块",
      description: "管理员可以进行图书录入、用户管理等基础管理操作",
      color: "from-emerald-500 to-teal-500",
      bgColor: "bg-gradient-to-br from-emerald-50 to-teal-50",
    },
    {
      icon: "🌟",
      title: "用户中心",
      subtitle: "个人信息管理",
      description: "用户可以查看个人借阅记录，管理个人信息",
      color: "from-orange-500 to-red-500",
      bgColor: "bg-gradient-to-br from-orange-50 to-red-50",
    },
  ];

  const stats = [
    { number: "500+", label: "图书收录", icon: "📖" },
    { number: "50+", label: "注册用户", icon: "👥" },
    { number: "95%", label: "系统稳定", icon: "⭐" },
    { number: "Java", label: "后端技术", icon: "☕" },
  ];

  if (!mounted) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 relative overflow-hidden">
      {/* 动态背景粒子 */}
      <div className="absolute inset-0">
        {[...Array(50)].map((_, i) => (
          <div
            key={i}
            className="absolute w-1 h-1 bg-white/20 rounded-full animate-pulse"
            style={{
              left: `${Math.random() * 100}%`,
              top: `${Math.random() * 100}%`,
              animationDelay: `${Math.random() * 3}s`,
              animationDuration: `${2 + Math.random() * 3}s`,
            }}
          />
        ))}
      </div>

      {/* 主要内容 */}
      <div className="relative z-10 min-h-screen flex flex-col">
        {/* 头部区域 */}
        <header className="pt-12 pb-8">
          <div className="max-w-6xl mx-auto px-6">
            <div className="text-center">
              {/* Logo动画 */}
              <div className="flex justify-center mb-8">
                <div className="relative group">
                  <div className="absolute -inset-4 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full blur-lg opacity-30 group-hover:opacity-50 transition-opacity duration-500"></div>
                  <div className="relative w-24 h-24 bg-gradient-to-r from-blue-500 to-purple-500 rounded-2xl flex items-center justify-center shadow-2xl transform group-hover:scale-110 transition-transform duration-500">
                    <Image
                      src="/logo.png"
                      alt="悦读图书馆"
                      width={60}
                      height={60}
                      className="rounded-xl"
                    />
                  </div>
                </div>
              </div>

              {/* 标题动画 */}
              <h1 className="text-6xl md:text-7xl font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-400 via-purple-400 to-pink-400 mb-6 animate-pulse">
                悦读图书馆
              </h1>

              <p className="text-xl md:text-2xl text-gray-300 mb-8 max-w-3xl mx-auto leading-relaxed">
                基于Java和React的数字图书馆管理系统
              </p>
            </div>
          </div>
        </header>

        {/* 特色功能轮播 */}
        <section className="flex-1 flex items-center">
          <div className="max-w-6xl mx-auto px-6 w-full">
            <div className="grid lg:grid-cols-2 gap-12 items-center">
              {/* 左侧：功能展示 */}
              <div className="space-y-8">
                <div
                  className={`p-8 rounded-3xl ${features[currentFeature].bgColor} backdrop-blur-sm border border-white/10 transform transition-all duration-700 hover:scale-105`}
                >
                  <div className="flex items-center mb-6">
                    <span className="text-4xl mr-4">
                      {features[currentFeature].icon}
                    </span>
                    <div>
                      <h3 className="text-2xl font-bold text-gray-800">
                        {features[currentFeature].title}
                      </h3>
                      <p className="text-gray-600">
                        {features[currentFeature].subtitle}
                      </p>
                    </div>
                  </div>
                  <p className="text-gray-700 text-lg leading-relaxed">
                    {features[currentFeature].description}
                  </p>
                </div>

                {/* 功能指示器 */}
                <div className="flex justify-center space-x-3">
                  {features.map((_, index) => (
                    <button
                      key={index}
                      onClick={() => setCurrentFeature(index)}
                      className={`w-3 h-3 rounded-full transition-all duration-300 ${
                        index === currentFeature
                          ? "bg-gradient-to-r from-blue-500 to-purple-500 scale-125"
                          : "bg-white/30 hover:bg-white/50"
                      }`}
                    />
                  ))}
                </div>
              </div>

              {/* 右侧：统计数据和操作 */}
              <div className="space-y-8">
                {/* 数据统计 */}
                <div className="grid grid-cols-2 gap-6">
                  {stats.map((stat, index) => (
                    <div
                      key={index}
                      className="bg-white/10 backdrop-blur-sm rounded-2xl p-6 border border-white/20 hover:bg-white/20 transition-all duration-300 group"
                    >
                      <div className="text-center">
                        <span className="text-3xl mb-2 block group-hover:scale-110 transition-transform duration-300">
                          {stat.icon}
                        </span>
                        <div className="text-3xl font-bold text-white mb-1">
                          {stat.number}
                        </div>
                        <div className="text-gray-300 text-sm">
                          {stat.label}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>

                {/* 行动按钮 */}
                <div className="space-y-4">
                  <div
                    className="w-full bg-gradient-to-r from-blue-500 to-purple-500 text-white py-4 px-8 rounded-2xl font-semibold text-lg hover:from-blue-600 hover:to-purple-600 transform hover:scale-105 transition-all duration-300 shadow-2xl flex items-center justify-center group"
                    onClick={() => {
                      if (token) {
                        router.push("/books");
                      } else {
                        router.push("/login");
                      }
                    }}
                  >
                    <span className="mr-2">📚</span>
                    立即开始阅读之旅
                    <span className="ml-2 group-hover:translate-x-1 transition-transform duration-300">
                      →
                    </span>
                  </div>

                  <Link
                    href="/register"
                    className="w-full bg-white/10 backdrop-blur-sm text-white py-4 px-8 rounded-2xl font-semibold text-lg border-2 border-white/30 hover:bg-white/20 hover:border-white/50 transform hover:scale-105 transition-all duration-300 flex items-center justify-center group"
                  >
                    <span className="mr-2">✨</span>
                    免费注册账户
                    <span className="ml-2 group-hover:translate-x-1 transition-transform duration-300">
                      →
                    </span>
                  </Link>
                </div>

                {/* 技术标签 */}
                <div className="flex flex-wrap gap-3 justify-center">
                  {[
                    "React",
                    "Next.js",
                    "TypeScript",
                    "Java Servlet",
                    "JDBC",
                  ].map((tech, index) => (
                    <span
                      key={index}
                      className="px-4 py-2 bg-white/10 backdrop-blur-sm rounded-full text-sm text-gray-300 border border-white/20 hover:bg-white/20 transition-all duration-300"
                    >
                      {tech}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* 底部 */}
        <footer className="py-8">
          <div className="max-w-6xl mx-auto px-6">
            <div className="text-center">
              <p className="text-gray-400 text-sm select-none">
                © 2025 悦读图书馆 · 期末课程设计作品
              </p>
            </div>
          </div>
        </footer>
      </div>
    </div>
  );
}
