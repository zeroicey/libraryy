import pymysql
import random
from datetime import datetime, timedelta

# 数据库连接配置
connection = pymysql.connect(
    host='localhost',
    user='root',
    password='123456',
    database='libraryy',
    charset='utf8mb4'
)

try:
    with connection.cursor() as cursor:
        # 获取所有用户ID和图书ID
        cursor.execute("SELECT id FROM users")
        user_ids = [row[0] for row in cursor.fetchall()]

        cursor.execute("SELECT id FROM books WHERE rest_number > 0")
        book_ids = [row[0] for row in cursor.fetchall()]

        if not user_ids or not book_ids:
            print("⚠️ 用户或图书数据不足，无法插入借阅记录。")
        else:
            for _ in range(100):
                user_id = random.choice(user_ids)
                book_id = random.choice(book_ids)

                # 借阅时间随机在最近 60 天内
                borrow_date = datetime.now() - timedelta(days=random.randint(0, 60))
                is_returned = random.choice([0, 1])
                return_date = (borrow_date + timedelta(days=random.randint(1, 30))) if is_returned else None

                sql = """
                    INSERT INTO borrow_records (user_id, book_id, borrow_date, return_date, returned)
                    VALUES (%s, %s, %s, %s, %s)
                """
                try:
                    cursor.execute(sql, (
                        user_id, book_id, borrow_date, return_date, is_returned
                    ))

                    # 如果已归还，则不做处理；如果未归还，图书剩余数 -1（保持数据一致性）
                    if not is_returned:
                        cursor.execute("UPDATE books SET rest_number = rest_number - 1 WHERE id = %s AND rest_number > 0", (book_id,))
                except pymysql.err.IntegrityError as e:
                    print(f"跳过无效记录: {e}")

            connection.commit()
            print("✅ 成功插入 100 条借阅记录。")

finally:
    connection.close()
