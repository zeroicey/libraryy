'use client';

import Link from 'next/link';
import Image from 'next/image';
import { useState } from 'react';

export function Navbar() {
  const [activeItem, setActiveItem] = useState('');

  const navItems = [
    { name: '阅读', href: '/reading', icon: '📖' },
    { name: '借阅', href: '/borrow', icon: '📚' },
    { name: '还书', href: '/return', icon: '📤' },
    { name: '书评', href: '/reviews', icon: '✍️' },
  ];

  return (
    <nav className="w-full bg-white/95 backdrop-blur-sm border-b border-gray-200 shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo和项目名称 */}
          <Link href="/" className="flex items-center space-x-3 group">
            <div className="relative">
              <div className="absolute -inset-1 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg blur opacity-25 group-hover:opacity-40 transition-opacity duration-300"></div>
              <div className="relative w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg flex items-center justify-center">
                <Image 
                  src="/logo.png" 
                  alt="悦读图书馆" 
                  width={24} 
                  height={24}
                  className="rounded-md"
                />
              </div>
            </div>
            <div className="flex flex-col">
              <h1 className="text-xl font-bold text-gray-800 group-hover:text-blue-600 transition-colors duration-300">
                悦读图书馆
              </h1>
              <p className="text-xs text-gray-500 hidden sm:block">数字图书馆管理系统</p>
            </div>
          </Link>

          {/* 导航菜单 */}
          <div className="hidden md:flex items-center space-x-1">
            {navItems.map((item) => (
              <Link
                key={item.name}
                href={item.href}
                onClick={() => setActiveItem(item.name)}
                className={`flex items-center space-x-2 px-4 py-2 rounded-lg text-sm font-medium transition-all duration-300 hover:bg-blue-50 hover:text-blue-600 ${
                  activeItem === item.name 
                    ? 'bg-blue-100 text-blue-600' 
                    : 'text-gray-700'
                }`}
              >
                <span className="text-lg">{item.icon}</span>
                <span>{item.name}</span>
              </Link>
            ))}
          </div>

          {/* 用户操作区域 */}
          <div className="flex items-center space-x-3">
            {/* 搜索按钮 */}
            <button className="p-2 text-gray-500 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-all duration-300">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </button>
            
            {/* 用户头像/登录 */}
            <div className="flex items-center space-x-2">
              <Link 
                href="/login"
                className="px-4 py-2 text-sm font-medium text-blue-600 hover:text-blue-700 transition-colors duration-300"
              >
                登录
              </Link>
              <Link 
                href="/register"
                className="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors duration-300"
              >
                注册
              </Link>
            </div>
          </div>

          {/* 移动端菜单按钮 */}
          <div className="md:hidden">
            <button className="p-2 text-gray-500 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-all duration-300">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      {/* 移动端导航菜单 */}
      <div className="md:hidden border-t border-gray-200 bg-white">
        <div className="px-4 py-3 space-y-1">
          {navItems.map((item) => (
            <Link
              key={item.name}
              href={item.href}
              onClick={() => setActiveItem(item.name)}
              className={`flex items-center space-x-3 px-3 py-2 rounded-lg text-sm font-medium transition-all duration-300 ${
                activeItem === item.name 
                  ? 'bg-blue-100 text-blue-600' 
                  : 'text-gray-700 hover:bg-blue-50 hover:text-blue-600'
              }`}
            >
              <span className="text-lg">{item.icon}</span>
              <span>{item.name}</span>
            </Link>
          ))}
        </div>
      </div>
    </nav>
  );
}
