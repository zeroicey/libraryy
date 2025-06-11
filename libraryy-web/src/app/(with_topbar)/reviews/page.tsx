"use client";

import React, { useState, useEffect, useMemo } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Star, MessageSquare, Search, Filter, PlusCircle } from "lucide-react";
import mockReviews from "@/mock/reviews.json";
import mockBooks from "@/mock/books.json";
import { toast } from "sonner";

interface Book {
  id: number;
  title: string;
  author: string;
  publisher: string;
  publish_date: string;
  isbn: string;
  category: string;
  cover_image_url: string;
  description: string;
  tags: string[];
  rating: number;
  stock: number;
  epub_url?: string;
}

interface Review {
  id: string;
  book_id: string;
  user_id: string;
  user_name: string;
  rating: number;
  comment: string;
  review_date: string;
}

interface ReviewWithBookTitle extends Review {
  book_title: string;
}

const ReviewsPage = () => {
  const [reviews, setReviews] = useState<ReviewWithBookTitle[]>([]);
  const [books, setBooks] = useState<Book[]>(mockBooks);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterBookId, setFilterBookId] = useState<string>("all");
  const [sortOrder, setSortOrder] = useState<string>("date_desc"); // date_asc, rating_desc, rating_asc

  const [isAddReviewModalOpen, setIsAddReviewModalOpen] = useState(false);
  const [newReviewData, setNewReviewData] = useState<Partial<Review>>({
    book_id: "",
    user_name: "",
    rating: 0,
    comment: "",
  });

  useEffect(() => {
    const reviewsWithTitles = mockReviews.map((review) => {
      const book = books.find((b) => b.id.toString() === review.book_id);
      return {
        ...review,
        book_title: book ? book.title : "未知书籍",
      };
    });
    setReviews(reviewsWithTitles);
  }, [books]);

  const filteredAndSortedReviews = useMemo(() => {
    let processedReviews = reviews;

    // Filtering
    if (filterBookId !== "all") {
      processedReviews = processedReviews.filter(
        (review) => review.book_id === filterBookId
      );
    }

    if (searchTerm) {
      processedReviews = processedReviews.filter(
        (review) =>
          review.book_title.toLowerCase().includes(searchTerm.toLowerCase()) ||
          review.user_name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          review.comment.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Sorting
    processedReviews.sort((a, b) => {
      switch (sortOrder) {
        case "date_asc":
          return new Date(a.review_date).getTime() - new Date(b.review_date).getTime();
        case "rating_desc":
          return b.rating - a.rating;
        case "rating_asc":
          return a.rating - b.rating;
        case "date_desc":
        default:
          return new Date(b.review_date).getTime() - new Date(a.review_date).getTime();
      }
    });

    return processedReviews;
  }, [reviews, searchTerm, filterBookId, sortOrder]);

  const handleAddReviewInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setNewReviewData((prev) => ({
      ...prev,
      [name]: name === "rating" ? parseInt(value, 10) : value,
    }));
  };
  
  const handleSelectChange = (value: string) => {
    setNewReviewData((prev) => ({
        ...prev,
        book_id: value,
    }));
  };

  const handleAddReviewSubmit = () => {
    if (!newReviewData.book_id || !newReviewData.user_name || newReviewData.rating === 0 || !newReviewData.comment) {
        toast.error("请填写所有必填项！");
        return;
    }
    const newReview: ReviewWithBookTitle = {
      id: `review_${Date.now().toString()}`,
      book_id: newReviewData.book_id!,
      user_id: `user_${Date.now().toString()}`,
      user_name: newReviewData.user_name!,
      rating: newReviewData.rating!,
      comment: newReviewData.comment!,
      review_date: new Date().toISOString().split("T")[0],
      book_title: books.find(b => b.id.toString() === newReviewData.book_id)?.title || "未知书籍",
    };
    setReviews((prev) => [newReview, ...prev]);
    toast.success("书评添加成功！");
    setIsAddReviewModalOpen(false);
    setNewReviewData({ book_id: "", user_name: "", rating: 0, comment: "" });
  };

  const renderStars = (rating: number) => {
    return Array(5)
      .fill(0)
      .map((_, i) => (
        <Star
          key={i}
          className={`h-5 w-5 ${i < rating ? "text-yellow-400 fill-yellow-400" : "text-gray-300"}`}
        />
      ));
  };

  return (
    <div className="container mx-auto p-4 md:p-6 lg:p-8">
      <header className="mb-6 flex flex-col md:flex-row justify-between items-center">
        <div>
            <h1 className="text-3xl font-bold text-gray-800">用户书评</h1>
            <p className="text-gray-600 mt-1">查看和分享对书籍的评价。</p>
        </div>
        <Dialog open={isAddReviewModalOpen} onOpenChange={setIsAddReviewModalOpen}>
            <DialogTrigger asChild>
                <Button className="mt-4 md:mt-0 bg-blue-600 hover:bg-blue-700">
                    <PlusCircle className="mr-2 h-5 w-5" /> 添加书评
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-lg">
                <DialogHeader>
                    <DialogTitle>添加新的书评</DialogTitle>
                    <DialogDescription>
                        分享您对这本书的看法和评分。
                    </DialogDescription>
                </DialogHeader>
                <div className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 items-center gap-4">
                        <label htmlFor="book_id" className="text-right col-span-1">书籍</label>
                        <Select name="book_id" value={newReviewData.book_id} onValueChange={handleSelectChange}>
                            <SelectTrigger className="col-span-3">
                                <SelectValue placeholder="选择一本书" />
                            </SelectTrigger>
                            <SelectContent>
                                {books.map(book => (
                                    <SelectItem key={book.id} value={book.id.toString()}>{book.title}</SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <label htmlFor="user_name" className="text-right col-span-1">昵称</label>
                        <Input id="user_name" name="user_name" value={newReviewData.user_name} onChange={handleAddReviewInputChange} className="col-span-3" placeholder="您的昵称" />
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <label htmlFor="rating" className="text-right col-span-1">评分</label>
                        <Select name="rating" value={newReviewData.rating?.toString()} onValueChange={(value) => setNewReviewData(prev => ({...prev, rating: parseInt(value)})) }>
                            <SelectTrigger className="col-span-3">
                                <SelectValue placeholder="选择评分" />
                            </SelectTrigger>
                            <SelectContent>
                                {[1,2,3,4,5].map(r => <SelectItem key={r} value={r.toString()}>{r} 星</SelectItem>)}
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <label htmlFor="comment" className="text-right col-span-1">评论</label>
                        <textarea id="comment" name="comment" value={newReviewData.comment} onChange={handleAddReviewInputChange} className="col-span-3 min-h-[100px] p-2 border rounded-md" placeholder="写下您的评论..." />
                    </div>
                </div>
                <DialogFooter>
                    <Button type="button" variant="outline" onClick={() => setIsAddReviewModalOpen(false)}>取消</Button>
                    <Button type="submit" onClick={handleAddReviewSubmit} className="bg-blue-600 hover:bg-blue-700">提交书评</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
      </header>

      <div className="mb-6 grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
        <div className="relative md:col-span-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
          <Input
            type="search"
            placeholder="搜索书名、用户或评论..."
            className="pl-10 w-full"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="md:col-span-1">
            <label htmlFor="filterBookId" className="block text-sm font-medium text-gray-700 mb-1">筛选书籍</label>
            <Select value={filterBookId} onValueChange={setFilterBookId}>
                <SelectTrigger className="w-full">
                    <SelectValue placeholder="按书籍筛选" />
                </SelectTrigger>
                <SelectContent>
                    <SelectItem value="all">所有书籍</SelectItem>
                    {books.map((book) => (
                    <SelectItem key={book.id} value={book.id.toString()}>
                        {book.title}
                    </SelectItem>
                    ))}
                </SelectContent>
            </Select>
        </div>
        <div className="md:col-span-1">
            <label htmlFor="sortOrder" className="block text-sm font-medium text-gray-700 mb-1">排序方式</label>
            <Select value={sortOrder} onValueChange={setSortOrder}>
                <SelectTrigger className="w-full">
                    <SelectValue placeholder="排序方式" />
                </SelectTrigger>
                <SelectContent>
                    <SelectItem value="date_desc">日期 (最新)</SelectItem>
                    <SelectItem value="date_asc">日期 (最早)</SelectItem>
                    <SelectItem value="rating_desc">评分 (高到低)</SelectItem>
                    <SelectItem value="rating_asc">评分 (低到高)</SelectItem>
                </SelectContent>
            </Select>
        </div>
      </div>

      {filteredAndSortedReviews.length > 0 ? (
        <div className="grid gap-6 md:grid-cols-1 lg:grid-cols-1">
          {filteredAndSortedReviews.map((review) => (
            <div key={review.id} className="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow">
              <div className="flex flex-col sm:flex-row justify-between sm:items-center mb-3">
                <h3 className="text-xl font-semibold text-blue-700 mb-1 sm:mb-0">
                  {review.book_title}
                </h3>
                <div className="flex items-center text-sm text-gray-500">
                  {renderStars(review.rating)}
                  <span className="ml-2">({review.rating}.0)</span>
                </div>
              </div>
              <p className="text-gray-700 mb-3 leading-relaxed">
                <MessageSquare className="inline-block mr-2 h-5 w-5 text-gray-400" />
                {review.comment}
              </p>
              <div className="text-sm text-gray-500 flex justify-between items-center pt-3 border-t border-gray-200">
                <span>
                  评论人: <span className="font-medium text-gray-700">{review.user_name}</span>
                </span>
                <span>
                  日期: <span className="font-medium text-gray-700">{review.review_date}</span>
                </span>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="text-center py-12">
          <Filter className="mx-auto h-12 w-12 text-gray-400 mb-4" />
          <p className="text-xl text-gray-600">没有找到匹配的书评。</p>
          <p className="text-gray-500 mt-2">尝试调整您的搜索或筛选条件，或者添加新的书评。</p>
        </div>
      )}
    </div>
  );
};

export default ReviewsPage;
