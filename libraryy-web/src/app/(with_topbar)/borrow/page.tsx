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
  PlusCircle,
  Search,
  Edit3,
  Trash2,
  BookOpen,
  CheckCircle,
  XCircle,
  CalendarDays,
  User,
  Info,
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
import { Label } from "@/components/ui/label";
// import { DatePicker } from '@/components/ui/date-picker'; // 改为手动输入日期

// 模拟数据导入 - 确保路径正确
import mockBorrows from "@/mock/borrows.json";
import mockBooks from "@/mock/books.json"; // 用于获取书籍详情

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

interface Book {
  id: string;
  title: string;
  author: string;
  isbn: string;
  // 添加其他你需要的书籍属性
}

const BorrowsPage = () => {
  const [borrows, setBorrows] = useState<BorrowRecord[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState<BorrowRecord | null>(null);
  const [isDeleteConfirmOpen, setIsDeleteConfirmOpen] = useState(false);
  const [recordToDelete, setRecordToDelete] = useState<BorrowRecord | null>(
    null
  );

  // 初始化加载数据
  useEffect(() => {
    // 在实际应用中，这里会从API获取数据
    // 为了演示，我们添加 overdue 状态的计算
    const today = new Date().toISOString().split("T")[0];
    const updatedBorrows = mockBorrows.map((record) => {
      if (record.status === "borrowed" && record.due_date < today) {
        return { ...record, status: "overdue" as "overdue" };
      }
      return record as BorrowRecord;
    });
    setBorrows(updatedBorrows);
  }, []);

  const filteredBorrows = useMemo(() => {
    return borrows
      .filter(
        (record) =>
          record.book_title.toLowerCase().includes(searchTerm.toLowerCase()) ||
          record.user_name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          record.id.toLowerCase().includes(searchTerm.toLowerCase())
      )
      .filter(
        (record) => statusFilter === "all" || record.status === statusFilter
      );
  }, [borrows, searchTerm, statusFilter]);

  const handleAddOrEdit = (record: BorrowRecord) => {
    if (editingRecord) {
      // 编辑
      setBorrows(borrows.map((r) => (r.id === record.id ? record : r)));
    } else {
      // 新增
      const newRecord = { ...record, id: `borrow_${Date.now()}` }; // 简单生成ID
      setBorrows([newRecord, ...borrows]);
    }
    setIsModalOpen(false);
    setEditingRecord(null);
  };

  const openEditModal = (record: BorrowRecord) => {
    setEditingRecord(record);
    setIsModalOpen(true);
  };

  const openAddModal = () => {
    setEditingRecord(null); // 清除编辑状态
    setIsModalOpen(true);
  };

  const handleDelete = (recordId: string) => {
    setBorrows(borrows.filter((r) => r.id !== recordId));
    setIsDeleteConfirmOpen(false);
    setRecordToDelete(null);
  };

  const openDeleteConfirm = (record: BorrowRecord) => {
    setRecordToDelete(record);
    setIsDeleteConfirmOpen(true);
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
        <h1 className="text-3xl font-bold text-gray-800">借阅管理</h1>
        <p className="text-gray-600 mt-1">管理图书的借阅和归还记录。</p>
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
        <div className="flex items-center gap-4">
          <Select value={statusFilter} onValueChange={setStatusFilter}>
            <SelectTrigger className="w-full md:w-[180px]">
              <SelectValue placeholder="筛选状态" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">所有状态</SelectItem>
              <SelectItem value="borrowed">借阅中</SelectItem>
              <SelectItem value="returned">已归还</SelectItem>
              <SelectItem value="overdue">已逾期</SelectItem>
            </SelectContent>
          </Select>
          <Button
            onClick={openAddModal}
            className="bg-blue-600 hover:bg-blue-700 text-white"
          >
            <PlusCircle className="mr-2 h-5 w-5" /> 新增借阅
          </Button>
        </div>
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
              <TableHead>归还日期</TableHead>
              <TableHead>状态</TableHead>
              <TableHead className="text-right">操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredBorrows.length > 0 ? (
              filteredBorrows.map((record) => (
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
                  <TableCell className="text-gray-600">
                    {record.return_date || "-"}
                  </TableCell>
                  <TableCell>{getStatusBadge(record.status)}</TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => openEditModal(record)}
                      title="编辑"
                    >
                      <Edit3 className="h-4 w-4 text-blue-500" />
                    </Button>
                    {record.status !== "returned" && (
                      <Button
                        variant="ghost"
                        size="icon"
                        onClick={() => {
                          const updatedRecord = {
                            ...record,
                            return_date: new Date().toISOString().split("T")[0],
                            status: "returned" as "returned",
                          };
                          handleAddOrEdit(updatedRecord);
                        }}
                        title="标记为已归还"
                      >
                        <CheckCircle className="h-4 w-4 text-green-500" />
                      </Button>
                    )}
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => openDeleteConfirm(record)}
                      title="删除"
                    >
                      <Trash2 className="h-4 w-4 text-red-500" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={8}
                  className="h-24 text-center text-gray-500"
                >
                  未找到相关借阅记录。
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      {/* 新增/编辑模态框 */}
      <BorrowFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleAddOrEdit}
        record={editingRecord}
        books={
          mockBooks.map((book) => ({
            ...book,
            id: String(book.id), // 将数字类型的id转换为字符串类型
          })) as unknown as Book[]
        } // 先转为unknown再转为Book[]类型
      />

      {/* 删除确认模态框 */}
      <Dialog open={isDeleteConfirmOpen} onOpenChange={setIsDeleteConfirmOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>确认删除</DialogTitle>
            <DialogDescription>
              您确定要删除这条借阅记录吗？此操作无法撤销。
            </DialogDescription>
          </DialogHeader>
          <DialogFooter className="sm:justify-start">
            <Button
              type="button"
              variant="destructive"
              onClick={() => recordToDelete && handleDelete(recordToDelete.id)}
            >
              删除
            </Button>
            <DialogClose asChild>
              <Button type="button" variant="outline">
                取消
              </Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

// 新增/编辑表单模态框组件
interface BorrowFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (record: BorrowRecord) => void;
  record: BorrowRecord | null; // 用于编辑，新增时为null
  books: Book[]; // 书籍列表
}

const BorrowFormModal: React.FC<BorrowFormModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  record,
  books,
}) => {
  const [formData, setFormData] = useState<Partial<BorrowRecord>>({});
  const [selectedBook, setSelectedBook] = useState<Book | null>(null);

  useEffect(() => {
    if (record) {
      setFormData(record);
      const book = books.find((b) => b.id === record.book_id);
      setSelectedBook(book || null);
    } else {
      // 新增时的默认值
      setFormData({
        borrow_date: new Date().toISOString().split("T")[0],
        due_date: new Date(Date.now() + 14 * 24 * 60 * 60 * 1000)
          .toISOString()
          .split("T")[0], // 默认14天后
        status: "borrowed",
      });
      setSelectedBook(null);
    }
  }, [record, isOpen, books]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSelectChange = (name: string, value: string) => {
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (name === "book_id") {
      const book = books.find((b) => b.id === value);
      setSelectedBook(book || null);
      if (book) {
        setFormData((prev) => ({ ...prev, book_title: book.title }));
      }
    }
  };

  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    // 简单的YYYY-MM-DD格式校验
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (value === "" || dateRegex.test(value)) {
      setFormData((prev) => ({ ...prev, [name]: value }));
    } else {
      // 可以添加更友好的提示，或者阻止无效输入
      console.warn("日期格式不正确，请输入 YYYY-MM-DD 格式");
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // 简单校验
    if (
      !formData.book_id ||
      !formData.user_name ||
      !formData.borrow_date ||
      !formData.due_date
    ) {
      alert("请填写所有必填项！");
      return;
    }
    onSubmit(formData as BorrowRecord);
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[525px]">
        <DialogHeader>
          <DialogTitle>{record ? "编辑借阅记录" : "新增借阅记录"}</DialogTitle>
          {record && (
            <DialogDescription>
              修改ID为 {record.id} 的借阅信息。
            </DialogDescription>
          )}
        </DialogHeader>
        <form onSubmit={handleSubmit} className="grid gap-6 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="book_id" className="text-right col-span-1">
              书籍名称
            </Label>
            <div className="col-span-3">
              <Select
                value={formData.book_id}
                onValueChange={(value) => handleSelectChange("book_id", value)}
                disabled={!!record} // 编辑时不允许修改书籍
              >
                <SelectTrigger id="book_id">
                  <SelectValue placeholder="选择一本书" />
                </SelectTrigger>
                <SelectContent>
                  {books.map((book) => (
                    <SelectItem key={book.id} value={book.id}>
                      {book.title} (ISBN: {book.isbn})
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {selectedBook && (
                <p className="text-xs text-gray-500 mt-1">
                  <Info size={12} className="inline mr-1" />
                  作者: {selectedBook.author}, ISBN: {selectedBook.isbn}
                </p>
              )}
            </div>
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="user_name" className="text-right col-span-1">
              借阅人姓名
            </Label>
            <Input
              id="user_name"
              name="user_name"
              value={formData.user_name || ""}
              onChange={handleChange}
              className="col-span-3"
              required
            />
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="user_id" className="text-right col-span-1">
              借阅人ID
            </Label>
            <Input
              id="user_id"
              name="user_id"
              value={formData.user_id || ""}
              onChange={handleChange}
              className="col-span-3"
              placeholder="例如：student_001, teacher_A"
            />
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="borrow_date" className="text-right col-span-1">
              借阅日期
            </Label>
            <Input
              id="borrow_date"
              name="borrow_date"
              type="text"
              value={formData.borrow_date || ""}
              onChange={handleDateChange}
              className="col-span-3"
              placeholder="YYYY-MM-DD"
              required
            />
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="due_date" className="text-right col-span-1">
              应还日期
            </Label>
            <Input
              id="due_date"
              name="due_date"
              type="text"
              value={formData.due_date || ""}
              onChange={handleDateChange}
              className="col-span-3"
              placeholder="YYYY-MM-DD"
              required
            />
          </div>

          {record && (
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="return_date" className="text-right col-span-1">
                归还日期
              </Label>
              <Input
                id="return_date"
                name="return_date"
                type="text"
                value={formData.return_date || ""}
                onChange={handleDateChange}
                className="col-span-3"
                placeholder="YYYY-MM-DD (可选)"
              />
            </div>
          )}

          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="status" className="text-right col-span-1">
              状态
            </Label>
            <Select
              value={formData.status}
              onValueChange={(value) => handleSelectChange("status", value)}
            >
              <SelectTrigger id="status" className="col-span-3">
                <SelectValue placeholder="选择状态" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="borrowed">借阅中</SelectItem>
                <SelectItem value="returned">已归还</SelectItem>
                <SelectItem value="overdue">已逾期</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <DialogFooter>
            <DialogClose asChild>
              <Button type="button" variant="outline">
                取消
              </Button>
            </DialogClose>
            <Button
              type="submit"
              className="bg-blue-600 hover:bg-blue-700 text-white"
            >
              {record ? "保存更改" : "确认新增"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default BorrowsPage;
