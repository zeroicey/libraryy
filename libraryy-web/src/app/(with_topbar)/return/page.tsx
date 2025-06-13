"use client";

import React, { useState, useEffect, useMemo } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import {
  Search,
  CheckCircle,
  AlertTriangle,
  BookUp,
} from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
  DialogClose,
} from "@/components/ui/dialog";
import mockBorrows from "@/mock/borrows.json"; // 确保路径正确
import { toast } from "sonner";

interface BorrowRecord {
  id: string;
  book_id: string;
  book_title: string;
  user_id: string;
  user_name: string;
  borrow_date: string;
  due_date: string;
  return_date: string | null;
  status: "borrowed" | "returned" | "overdue";
}

const ReturnPage = () => {
  const [borrows, setBorrows] = useState<BorrowRecord[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("borrowed_overdue"); // Default to borrowed and overdue
  const [confirmReturnRecord, setConfirmReturnRecord] = useState<BorrowRecord | null>(null);

  useEffect(() => {
    const today = new Date().toISOString().split("T")[0];
    const initialBorrows = mockBorrows.map((record) => {
      if (record.status === "borrowed" && record.due_date < today) {
        return { ...record, status: "overdue" as "overdue" };
      }
      return record as BorrowRecord;
    });
    setBorrows(initialBorrows);
  }, []);

  const recordsToReturn = useMemo(() => {
    return borrows
      .filter((record) =>
        record.book_title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        record.user_name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        record.id.toLowerCase().includes(searchTerm.toLowerCase())
      )
      .filter((record) => {
        if (statusFilter === "all") return true;
        if (statusFilter === "borrowed_overdue") return record.status === "borrowed" || record.status === "overdue";
        return record.status === statusFilter;
      });
  }, [borrows, searchTerm, statusFilter]);

  const handleReturnBook = (recordId: string) => {
    const today = new Date().toISOString().split("T")[0];
    setBorrows((prevBorrows) =>
      prevBorrows.map((record) =>
        record.id === recordId
          ? { ...record, status: "returned", return_date: today }
          : record
      )
    );
    toast.success(`书籍 "${confirmReturnRecord?.book_title}" 已成功归还。`);
    setConfirmReturnRecord(null);
  };

  const openReturnConfirm = (record: BorrowRecord) => {
    setConfirmReturnRecord(record);
  };

  const getStatusBadge = (status: BorrowRecord["status"]) => {
    switch (status) {
      case "borrowed":
        return (
          <span className="px-2 py-1 text-xs font-semibold text-blue-700 bg-blue-100 rounded-full">
            借阅中
          </span>
        );
      case "returned":
        return (
          <span className="px-2 py-1 text-xs font-semibold text-green-700 bg-green-100 rounded-full">
            已归还
          </span>
        );
      case "overdue":
        return (
          <span className="px-2 py-1 text-xs font-semibold text-red-700 bg-red-100 rounded-full">
            已逾期
          </span>
        );
      default:
        return null;
    }
  };

  return (
    <div className="container mx-auto p-4 md:p-6 lg:p-8">
      <header className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">还书管理</h1>
        <p className="text-gray-600 mt-1">处理图书的归还操作。</p>
      </header>

      <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="relative flex-grow md:max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-400" />
          <Input
            type="search"
            placeholder="搜索书名、用户或ID..."
            className="pl-10 w-full"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <Select value={statusFilter} onValueChange={setStatusFilter}>
          <SelectTrigger className="w-full md:w-[200px]">
            <SelectValue placeholder="筛选状态" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">所有状态</SelectItem>
            <SelectItem value="borrowed_overdue">借阅中/已逾期</SelectItem>
            <SelectItem value="borrowed">借阅中</SelectItem>
            <SelectItem value="overdue">已逾期</SelectItem>
            <SelectItem value="returned">已归还</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-gray-50">
              <TableHead className="w-[100px]">记录ID</TableHead>
              <TableHead>书籍名称</TableHead>
              <TableHead>借阅人</TableHead>
              <TableHead>借阅日期</TableHead>
              <TableHead>应还日期</TableHead>
              <TableHead>状态</TableHead>
              <TableHead className="text-right">操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {recordsToReturn.length > 0 ? (
              recordsToReturn.map((record) => (
                <TableRow
                  key={record.id}
                  className="hover:bg-gray-50 transition-colors"
                >
                  <TableCell className="font-medium text-gray-700">
                    {record.id.substring(0, 10)}...
                  </TableCell>
                  <TableCell className="text-gray-600">
                    {record.book_title}
                  </TableCell>
                  <TableCell className="text-gray-600">
                    {record.user_name}
                  </TableCell>
                  <TableCell className="text-gray-600">
                    {record.borrow_date}
                  </TableCell>
                  <TableCell className="text-gray-600">
                    {record.due_date}
                  </TableCell>
                  <TableCell>{getStatusBadge(record.status)}</TableCell>
                  <TableCell className="text-right">
                    {(record.status === "borrowed" || record.status === "overdue") && (
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => openReturnConfirm(record)}
                        className="text-green-600 border-green-600 hover:bg-green-50 hover:text-green-700"
                      >
                        <BookUp className="mr-2 h-4 w-4" /> 归还
                      </Button>
                    )}
                    {record.status === "returned" && (
                       <span className="text-sm text-gray-500 italic">已处理</span>
                    )}
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={7} className="text-center text-gray-500 py-8">
                  没有找到符合条件的借阅记录。
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      {/* 归还确认弹窗 */}      
      <Dialog open={!!confirmReturnRecord} onOpenChange={() => setConfirmReturnRecord(null)}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle className="flex items-center">
              <AlertTriangle className="mr-2 h-6 w-6 text-yellow-500" />
              确认归还书籍？
            </DialogTitle>
            <DialogDescription className="pt-2">
              您确定要将书籍 "<strong>{confirmReturnRecord?.book_title}</strong>" 
              (借阅人: {confirmReturnRecord?.user_name}) 标记为已归还吗？此操作将会更新归还日期。
            </DialogDescription>
          </DialogHeader>
          <DialogFooter className="mt-4 sm:justify-end gap-2">
            <Button
              variant="outline"
              onClick={() => setConfirmReturnRecord(null)}
            >
              取消
            </Button>
            <Button
              onClick={() => handleReturnBook(confirmReturnRecord!.id)}
              className="bg-green-600 hover:bg-green-700 text-white"
            >
              <CheckCircle className="mr-2 h-4 w-4" /> 确认归还
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default ReturnPage;
