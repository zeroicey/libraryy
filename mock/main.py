import pymysql
from faker import Faker
import hashlib
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
            username = fake.user_name() + str(random.randint(100, 999))
            password = fake.password(length=10)
            email = fake.unique.email()

            sql = """
                INSERT INTO users (username, password, email)
                VALUES (%s, %s, %s)
            """
            try:
                cursor.execute(sql, (username, password, email))
            except pymysql.err.IntegrityError as e:
                print(f"跳过重复用户/email: {e}")
        connection.commit()
        print("✅ 成功插入 100 条数据。")
finally:
    connection.close()
