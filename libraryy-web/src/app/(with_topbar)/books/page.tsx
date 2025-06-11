"use client";

import Link from "next/link";
import { X } from "lucide-react"; // 导入 X 图标
import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import {
  Book,
  Search,
  Filter,
  ChevronDown,
  Star,
  Tag,
  Users,
  CalendarDays,
  Library,
  BookText,
} from "lucide-react";
import booksData from "@/mock/books.json"; // 确保路径正确
import { useUserStore } from "@/store/user";
import { useRouter } from "next/navigation";

interface Book {
  id: number;
  title: string;
  author: string;
  publisher: string;
  isbn: string;
  description: string;
  category: string;
  publishDate: string;
  totalPages: number;
  stock: number;
  epub_url?: string; // 添加 epub_url 字段
}

const BookDetailModal = ({ book, onClose }: { book: Book | null; onClose: () => void }) => {
  if (!book) return null;

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4"
      onClick={onClose}
    >
      <motion.div
        initial={{ scale: 0.9, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0.9, opacity: 0 }}
        transition={{ type: "spring", stiffness: 300, damping: 30 }}
        className="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto p-6 sm:p-8 relative"
        onClick={(e) => e.stopPropagation()} // 防止点击模态框内容时关闭
      >
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-500 hover:text-gray-800 transition-colors p-1 rounded-full hover:bg-gray-100"
          aria-label="关闭详情"
        >
          <X size={24} />
        </button>

        <h2 className="text-3xl font-bold text-gray-800 mb-4 pr-10">{book.title}</h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4 mb-6 text-sm">
          <div className="flex items-center">
            <Users className="w-5 h-5 mr-2 text-sky-600 flex-shrink-0" />
            <span className="text-gray-700 font-medium">作者:</span>
            <span className="text-gray-600 ml-2 truncate">{book.author}</span>
          </div>
          <div className="flex items-center">
            <Library className="w-5 h-5 mr-2 text-indigo-600 flex-shrink-0" />
            <span className="text-gray-700 font-medium">出版社:</span>
            <span className="text-gray-600 ml-2 truncate">{book.publisher}</span>
          </div>
          <div className="flex items-center">
            <Tag className="w-5 h-5 mr-2 text-purple-600 flex-shrink-0" />
            <span className="text-gray-700 font-medium">分类:</span>
            <span className="text-gray-600 ml-2">{book.category}</span>
          </div>
          <div className="flex items-center">
            <CalendarDays className="w-5 h-5 mr-2 text-red-600 flex-shrink-0" />
            <span className="text-gray-700 font-medium">出版日期:</span>
            <span className="text-gray-600 ml-2">{book.publishDate}</span>
          </div>
          <div className="flex items-center">
            <BookText className="w-5 h-5 mr-2 text-green-600 flex-shrink-0" />
            <span className="text-gray-700 font-medium">ISBN:</span>
            <span className="text-gray-600 ml-2">{book.isbn}</span>
          </div>
          <div className="flex items-center">
            <Star className="w-5 h-5 mr-2 text-yellow-500 flex-shrink-0" /> 
            <span className="text-gray-700 font-medium">页数:</span>
            <span className="text-gray-600 ml-2">{book.totalPages}</span>
          </div>
           <div className="flex items-center md:col-span-2">
            <div className="bg-blue-500 text-white text-sm font-semibold px-3 py-1.5 rounded-full shadow-md">
              库存: {book.stock}
            </div>
          </div>
        </div>

        <div className="mb-6">
          <h3 className="text-xl font-semibold text-gray-700 mb-2">简介</h3>
          <p className="text-gray-600 leading-relaxed whitespace-pre-wrap text-sm">
            {book.description}
          </p>
        </div>

        <div className="flex flex-col sm:flex-row gap-3 mt-8 pt-6 border-t border-gray-200 items-center">
          {book.epub_url && (
            <Link href={`/reading?url=${encodeURIComponent(book.epub_url)}`} className="w-full sm:w-auto flex-grow sm:flex-grow-0">
              <motion.button
                whileHover={{ scale: 1.03, boxShadow: "0px 5px 15px rgba(0,0,0,0.1)" }}
                whileTap={{ scale: 0.97 }}
                className="w-full bg-green-500 hover:bg-green-600 text-white font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 flex items-center justify-center text-sm"
              >
                <BookText className="w-4 h-4 mr-2" /> 在线阅读
              </motion.button>
            </Link>
          )}
          <motion.button
            onClick={onClose}
            whileHover={{ scale: 1.03, boxShadow: "0px 5px 15px rgba(0,0,0,0.1)" }}
            whileTap={{ scale: 0.97 }}
            className={`w-full ${book.epub_url ? 'sm:w-auto flex-grow' : 'sm:w-full'} bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 flex items-center justify-center text-sm`}
          >
            关闭
          </motion.button>
        </div>
      </motion.div>
    </motion.div>
  );
};

const BooksPage = () => {
  const router = useRouter();
  const [books, setBooks] = useState<Book[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [sortBy, setSortBy] = useState("title");
  const [isLoading, setIsLoading] = useState(true);
  const [selectedBook, setSelectedBook] = useState<Book | null>(null);

  useEffect(() => {
    // 模拟数据加载
    setTimeout(() => {
      setBooks(booksData as Book[]);
      setIsLoading(false);
    }, 500);
  }, []);

  const categories = [
    "all",
    ...new Set(booksData.map((book) => book.category)),
  ];

  const filteredAndSortedBooks = books
    .filter((book) => {
      const matchesSearch =
        book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.author.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesCategory =
        selectedCategory === "all" || book.category === selectedCategory;
      return matchesSearch && matchesCategory;
    })
    .sort((a, b) => {
      if (sortBy === "title") {
        return a.title.localeCompare(b.title);
      }
      if (sortBy === "publishDate") {
        return (
          new Date(b.publishDate).getTime() - new Date(a.publishDate).getTime()
        );
      }
      if (sortBy === "stock") {
        return b.stock - a.stock;
      }
      return 0;
    });

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: (i: number) => ({
      opacity: 1,
      y: 0,
      transition: {
        delay: i * 0.05,
        duration: 0.3,
        ease: "easeOut",
      },
    }),
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-100 to-sky-100">
        <div className="flex flex-col items-center">
          <Book className="w-16 h-16 text-blue-600 animate-bounce" />
          <p className="mt-4 text-xl font-semibold text-gray-700">
            正在加载书籍...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 to-sky-100 p-4 sm:p-6 lg:p-8">
      <motion.header
        initial={{ opacity: 0, y: -30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, ease: "easeOut" }}
        className="mb-10 text-center"
      >
        <h1 className="text-3xl sm:text-4xl font-bold text-gray-800">
          悦读时光，智启未来
        </h1>
        <p className="mt-2 text-md text-gray-500">
          在知识的海洋中遨游，发现属于您的精神食粮
        </p>
      </motion.header>

      {/* 搜索和过滤区域 */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.4 }}
        className="mb-8 p-6 bg-white/80 backdrop-blur-md rounded-xl shadow-lg border border-gray-200"
      >
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
          <div className="md:col-span-2">
            <label
              htmlFor="search"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              搜索书籍
            </label>
            <div className="relative">
              <input
                type="text"
                id="search"
                placeholder="书名、作者..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200 shadow-sm"
              />
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label
                htmlFor="category"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                分类
              </label>
              <div className="relative">
                <select
                  id="category"
                  value={selectedCategory}
                  onChange={(e) => setSelectedCategory(e.target.value)}
                  className="w-full pl-3 pr-10 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200 shadow-sm appearance-none bg-white"
                >
                  {categories.map((category) => (
                    <option key={category} value={category}>
                      {category === "all" ? "所有分类" : category}
                    </option>
                  ))}
                </select>
                <Filter className="absolute right-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400 pointer-events-none" />
              </div>
            </div>
            <div>
              <label
                htmlFor="sort"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                排序
              </label>
              <div className="relative">
                <select
                  id="sort"
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value)}
                  className="w-full pl-3 pr-10 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200 shadow-sm appearance-none bg-white"
                >
                  <option value="title">书名</option>
                  <option value="publishDate">出版日期</option>
                  <option value="stock">库存</option>
                </select>
                <ChevronDown className="absolute right-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400 pointer-events-none" />
              </div>
            </div>
          </div>
        </div>
      </motion.div>

      {/* 书籍列表 */}
      {filteredAndSortedBooks.length > 0 ? (
        <motion.div
          className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 lg:gap-8"
          initial="hidden"
          animate="visible"
        >
          {filteredAndSortedBooks.map((book, i) => (
            <motion.div
              key={book.id}
              custom={i}
              variants={cardVariants}
              className="bg-white rounded-xl shadow-lg overflow-hidden transform transition-all duration-300 hover:shadow-2xl hover:-translate-y-1 border border-gray-200 flex flex-col p-5"
            >
              <div className="flex justify-between items-start mb-2">
                <h3 className="text-xl font-bold text-gray-800 group-hover:text-blue-600 transition-colors">
                  {book.title}
                </h3>
                <div className="bg-blue-500 text-white text-xs font-semibold px-2 py-1 rounded-full shadow-md whitespace-nowrap">
                  库存: {book.stock}
                </div>
              </div>
              <div className="flex flex-col flex-grow">
                <p className="text-sm text-gray-600 mb-1 flex items-center">
                  <Users className="w-4 h-4 mr-2 text-sky-500" /> {book.author}
                </p>
                <p className="text-xs text-gray-500 mb-3 flex items-center">
                  <Tag className="w-3.5 h-3.5 mr-2 text-purple-500" />{" "}
                  {book.category}
                </p>

                <p className="text-sm text-gray-700 mb-4 line-clamp-3 flex-grow">
                  {book.description}
                </p>

                <div className="mt-auto pt-4 border-t border-gray-200">
                  <div className="flex justify-between items-center text-xs text-gray-500 mb-2">
                    <span className="flex items-center">
                      <Library className="w-3.5 h-3.5 mr-1.5 text-gray-400" />
                      {book.publisher}
                    </span>
                    <span className="flex items-center">
                      <CalendarDays className="w-3.5 h-3.5 mr-1.5 text-gray-400" />
                      {book.publishDate}
                    </span>
                  </div>
                  <div className="flex gap-2 w-full">
                    <motion.button
                      onClick={() => setSelectedBook(book)}
                      whileHover={{
                        scale: 1.05,
                        backgroundColor: "#2563EB" /* blue-600 */,
                      }}
                      whileTap={{ scale: 0.95 }}
                      className="flex-1 w-full bg-blue-500 text-white font-semibold py-2.5 px-4 rounded-lg shadow-md hover:bg-blue-600 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 flex items-center justify-center"
                    >
                      <Book className="w-4 h-4 mr-2" /> 查看详情
                    </motion.button>
                    {book.epub_url && (
                      <div
                        onClick={() => {
                          useUserStore
                            .getState()
                            .setCurrentBook(book.epub_url!);
                          router.push("/reading");
                        }}
                        className="flex-1"
                      >
                        <motion.button
                          whileHover={{
                            scale: 1.05,
                            backgroundColor: "#16A34A" /* green-600 */,
                          }}
                          whileTap={{ scale: 0.95 }}
                          className="w-full bg-green-500 text-white font-semibold py-2.5 px-4 rounded-lg shadow-md hover:bg-green-600 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-opacity-50 flex items-center justify-center"
                        >
                          <BookText className="w-4 h-4 mr-2" /> 在线阅读
                        </motion.button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </motion.div>
          ))}
        </motion.div>
      ) : (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="text-center py-12"
        >
          <Search className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-2xl font-semibold text-gray-700 mb-2">
            未找到匹配的书籍
          </h3>
          <p className="text-gray-500">请尝试调整搜索条件或分类。</p>
        </motion.div>
      )}

      {/* 页脚 (可选) */}
      <footer className="mt-12 text-center text-sm text-gray-500">
        <p>&copy; {new Date().getFullYear()} 悦读图书馆. 版权所有.</p>
      </footer>

      <BookDetailModal book={selectedBook} onClose={() => setSelectedBook(null)} />
    </div>
  );
};

export default BooksPage;
