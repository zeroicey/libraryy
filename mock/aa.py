import pymysql
from faker import Faker
import random

# 初始化 Faker
fake = Faker()

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
        for _ in range(100):
            title = fake.unique.sentence(nb_words=4).replace(".", "")
            author = fake.name()
            tag = random.choice(['文学', '科技', '历史', '编程', '小说', '传记', '心理'])
            description = fake.paragraph(nb_sentences=3)
            cover_image = fake.image_url()
            isbn = fake.unique.isbn13(separator="")
            sum_number = random.randint(1, 20)
            rest_number = random.randint(0, sum_number)
            status = 'available' if rest_number > 0 else 'borrowed'

            sql = """
                INSERT INTO books (
                    title, author, tag, status, description,
                    cover_image, isbn, sum_number, rest_number
                )
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            try:
                cursor.execute(sql, (
                    title, author, tag, status, description,
                    cover_image, isbn, sum_number, rest_number
                ))
            except pymysql.err.IntegrityError as e:
                print(f"跳过重复书名/ISBN: {e}")
        connection.commit()
        print("✅ 成功插入 100 条图书数据。")
finally:
    connection.close()
