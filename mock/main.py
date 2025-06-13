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
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor  #  To get results as dictionaries
)

# Function to insert users
def insert_users(cursor, num_users=10):
    users_data = []
    for _ in range(num_users):
        username = fake.user_name()
        password = fake.password() # Store plain text password
        email = fake.email()
        users_data.append((username, password, email))
    
    sql = "INSERT INTO users (username, password, email) VALUES (%s, %s, %s)"
    cursor.executemany(sql, users_data)
    print(f"{num_users} users inserted.")

# Function to insert books
def insert_books(cursor, num_books=50):
    books_data = []
    for _ in range(num_books):
        title = fake.catch_phrase()
        author = fake.name()
        tag = fake.word()
        description = fake.text()
        cover_image = fake.image_url()
        isbn = fake.isbn13()
        sum_number = random.randint(1, 10)
        rest_number = random.randint(0, sum_number)
        status = 'available' if rest_number > 0 else 'borrowed'
        books_data.append((title, author, tag, status, description, cover_image, isbn, sum_number, rest_number))
    
    sql = "INSERT INTO books (title, author, tag, status, description, cover_image, isbn, sum_number, rest_number) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)"
    cursor.executemany(sql, books_data)
    print(f"{num_books} books inserted.")

# Function to insert borrow records
def insert_borrow_records(cursor, num_records=30):
    cursor.execute("SELECT id FROM users")
    user_ids = [row['id'] for row in cursor.fetchall()]
    cursor.execute("SELECT id FROM books")
    book_ids = [row['id'] for row in cursor.fetchall()]

    if not user_ids or not book_ids:
        print("No users or books found to create borrow records.")
        return

    borrow_records_data = []
    for _ in range(num_records):
        user_id = random.choice(user_ids)
        book_id = random.choice(book_ids)
        borrow_date = fake.date_time_this_year()
        return_date = None
        returned = 0
        # Simulate some returns
        if random.random() < 0.7: # 70% chance of being returned
            return_date = fake.date_time_between_dates(datetime_start=borrow_date)
            returned = 1
        
        borrow_records_data.append((user_id, book_id, borrow_date, return_date, returned))

    sql = "INSERT INTO borrow_records (user_id, book_id, borrow_date, return_date, returned) VALUES (%s, %s, %s, %s, %s)"
    cursor.executemany(sql, borrow_records_data)
    print(f"{num_records} borrow records inserted.")

# Function to insert reviews
def insert_reviews(cursor, num_reviews=100):
    cursor.execute("SELECT id FROM users")
    user_ids = [row['id'] for row in cursor.fetchall()]
    cursor.execute("SELECT id FROM books")
    book_ids = [row['id'] for row in cursor.fetchall()]

    if not user_ids or not book_ids:
        print("No users or books found to create reviews.")
        return

    reviews_data = []
    # Keep track of (user_id, book_id) pairs to ensure uniqueness
    reviewed_pairs = set()
    attempts = 0
    max_attempts = num_reviews * 5 # Avoid infinite loop if not enough unique pairs

    while len(reviews_data) < num_reviews and attempts < max_attempts:
        user_id = random.choice(user_ids)
        book_id = random.choice(book_ids)
        if (user_id, book_id) not in reviewed_pairs:
            rating = random.randint(1, 5)
            comment = fake.paragraph()
            reviews_data.append((user_id, book_id, rating, comment))
            reviewed_pairs.add((user_id, book_id))
        attempts += 1

    if reviews_data:
        sql = "INSERT INTO reviews (user_id, book_id, rating, comment) VALUES (%s, %s, %s, %s)"
        cursor.executemany(sql, reviews_data)
        print(f"{len(reviews_data)} reviews inserted.")
    else:
        print("No unique user-book pairs available to insert reviews or max attempts reached.")

# Function to insert categories
def insert_categories(cursor, num_categories=10):
    categories_data = []
    for _ in range(num_categories):
        name = fake.word().capitalize() # Simple capitalized word for category
        # Ensure uniqueness for category names if needed, though Faker might generate duplicates
        categories_data.append((name,))
    
    # To handle potential duplicate names from Faker, we can try to insert one by one or use INSERT IGNORE
    # For simplicity, let's assume names are mostly unique or duplicates are acceptable for mock data
    sql = "INSERT INTO categories (name) VALUES (%s)"
    try:
        cursor.executemany(sql, categories_data)
        print(f"{num_categories} categories inserted (some might be duplicates if names clash).")
    except pymysql.err.IntegrityError as e:
        print(f"Error inserting categories, possibly due to duplicate names: {e}")
        print("Consider adding unique handling if this is an issue.")

# Function to insert book_categories (many-to-many relationship)
def insert_book_categories(cursor, num_relations_per_book_avg=2):
    cursor.execute("SELECT id FROM books")
    book_ids = [row['id'] for row in cursor.fetchall()]
    cursor.execute("SELECT id FROM categories")
    category_ids = [row['id'] for row in cursor.fetchall()]

    if not book_ids or not category_ids:
        print("No books or categories found to create book-category relationships.")
        return

    book_categories_data = []
    # Keep track of (book_id, category_id) pairs to ensure uniqueness
    assigned_pairs = set()
    
    for book_id in book_ids:
        num_categories_for_book = random.randint(1, num_relations_per_book_avg + 1) # Each book gets 1 to N categories
        # Shuffle categories to pick random ones for each book
        random.shuffle(category_ids)
        assigned_count = 0
        for category_id in category_ids:
            if assigned_count >= num_categories_for_book:
                break
            if (book_id, category_id) not in assigned_pairs:
                book_categories_data.append((book_id, category_id))
                assigned_pairs.add((book_id, category_id))
                assigned_count +=1

    if book_categories_data:
        sql = "INSERT INTO book_categories (book_id, category_id) VALUES (%s, %s)"
        cursor.executemany(sql, book_categories_data)
        print(f"{len(book_categories_data)} book-category relationships inserted.")
    else:
        print("No book-category relationships were generated.")

try:
    with connection.cursor() as cursor:
        # Clear existing data (optional, be careful with this on real databases)
        # cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")
        # cursor.execute("TRUNCATE TABLE book_categories;")
        # cursor.execute("TRUNCATE TABLE categories;")
        # cursor.execute("TRUNCATE TABLE reviews;")
        # cursor.execute("TRUNCATE TABLE borrow_records;")
        # cursor.execute("TRUNCATE TABLE books;")
        # cursor.execute("TRUNCATE TABLE users;")
        # cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")
        # print("Existing data cleared.")

        # Insert data
        insert_users(cursor, num_users=20)
        insert_books(cursor, num_books=100)
        insert_borrow_records(cursor, num_records=150)
        insert_reviews(cursor, num_reviews=200) # Attempt to insert more reviews
        insert_categories(cursor, num_categories=15)
        insert_book_categories(cursor)

    connection.commit()
    print("Mock data inserted successfully!")

except pymysql.MySQLError as e:
    print(f"Error: {e}")
    connection.rollback()

finally:
    if connection:
        connection.close()
        print("Database connection closed.")