"use client";

import Link from "next/link";
import Image from "next/image";
import { useState } from "react";
import { useUserStore } from "@/store/user";
import {
  BookOpen,
  Book,
  BookCopy,
  ArrowLeftRight,
  MessageSquare,
  LogOut,
} from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { toast } from "sonner";
import { useRouter } from "next/navigation";

export default function Navbar() {
  const router = useRouter();
  const { username, email, avatar } = useUserStore();
  const [activeItem, setActiveItem] = useState("");
  const navItems = [
    { name: "书籍", href: "/books", icon: Book },
    { name: "阅读", href: "/reading", icon: BookOpen },
    { name: "借阅", href: "/borrow", icon: BookCopy },
    { name: "还书", href: "/return", icon: ArrowLeftRight },
    { name: "书评", href: "/reviews", icon: MessageSquare },
  ];
  return (
    <nav className="w-full bg-white/95 backdrop-blur-sm border-b border-gray-200 shadow-sm sticky top-0 z-50 py-3 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto flex justify-between items-center">
        <Link
          href="/"
          className="flex items-center space-x-3 group transition-all duration-300"
        >
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
          <div className="flex-col hidden sm:flex">
            <h1 className="text-xl font-bold text-gray-800 group-hover:text-blue-600 transition-colors duration-300 hidden md:block">
              悦读图书馆
            </h1>
            <p className="text-xs text-gray-500 hidden md:block">
              数字图书馆管理系统
            </p>
          </div>
        </Link>
        <div className="flex items-center space-x-1">
          {navItems.map((item) => (
            <Link
              key={item.name}
              href={item.href}
              onClick={() => setActiveItem(item.name)}
              className={`flex items-center space-x-2 px-4 py-2 rounded-lg text-sm font-medium transition-all duration-300 hover:bg-blue-50 hover:text-blue-600 ${
                activeItem === item.name
                  ? "bg-blue-100 text-blue-600"
                  : "text-gray-700"
              }`}
            >
              <span className="text-lg">
                {item.icon && (
                  <item.icon
                    size={20}
                    className="transition-transform duration-300 group-hover:scale-110"
                  />
                )}
              </span>
              <span className="hidden sm:block">{item.name}</span>
            </Link>
          ))}
        </div>
        <div className="flex justify-center items-center gap-2">
          <Avatar className="h-8 w-8 rounded-lg">
            <AvatarImage src={avatar} alt={username} />
            <AvatarFallback className="rounded-lg">YY</AvatarFallback>
          </Avatar>
          <div className="grid flex-1 text-left text-sm leading-tight">
            <span className="truncate font-medium">{username}</span>
            <span className="truncate text-xs">{email}</span>
          </div>
          <LogOut
            className="ml-auto size-4 cursor-pointer"
            onClick={() => {
              useUserStore.getState().logout();
              toast.success("欢迎再次使用");
              router.push("/login");
            }}
          />
        </div>
      </div>
    </nav>
  );
}
