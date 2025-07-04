create database if not exists libraryy;

use libraryy;

create table if not exists `users` (
       `id` int not null auto_increment,
       `username` varchar(100) not null,
       `password` varchar(255) not null,
       `email` varchar(100) not null,
       `created_at` datetime default current_timestamp,
       `updated_at` datetime default current_timestamp on update current_timestamp,

       primary key (`id`),
       unique key `username_unique` (`username`),
       unique key `email_unique` (`email`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table if not exists `books` (
       `id` int not null auto_increment,
       `title` varchar(100) not null,
       `author` varchar(255) not null,
       `tag` varchar(100) not null,
       `status` enum('available', 'borrowed') default 'available',
       `description` text not null,
       `cover_image` varchar(255),
       `isbn` varchar(20) not null, -- International Standard Book Number
       `sum_number` int not null,
       `rest_number` int not null,
       `created_at` datetime default current_timestamp,
       `updated_at` datetime default current_timestamp on update current_timestamp,

       primary key (`id`),
       unique key `title_unique` (`title`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table if not exists `borrow_records` (
    `id` int not null auto_increment,
    `user_id` int not null,
    `book_id` int not null,
    `borrow_date` datetime default current_timestamp,
    `return_date` datetime,
    `returned` tinyint(1) default 0,

    primary key (`id`),
    foreign key (`user_id`) references `users`(`id`),
    foreign key (`book_id`) references `books`(`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table if not exists `reviews` (
    `id` int not null auto_increment,
    `user_id` int not null,
    `book_id` int not null,
    `rating` tinyint check (rating >= 1 and rating <= 5), -- Rating from 1 to 5
    `comment` text,
    `created_at` datetime default current_timestamp,
    `updated_at` datetime default current_timestamp on update current_timestamp,

    primary key (`id`),
    foreign key (`user_id`) references `users`(`id`) on delete cascade,
    foreign key (`book_id`) references `books`(`id`) on delete cascade,
    unique key `user_book_review_unique` (`user_id`, `book_id`) -- A user can only review a book once
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table if not exists `categories` (
    `id` int not null auto_increment,
    `name` varchar(100) not null,
    `created_at` datetime default current_timestamp,
    `updated_at` datetime default current_timestamp on update current_timestamp,

    primary key (`id`),
    unique key `name_unique` (`name`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table if not exists `book_categories` (
    `book_id` int not null,
    `category_id` int not null,
    `created_at` datetime default current_timestamp,

    primary key (`book_id`, `category_id`),
    foreign key (`book_id`) references `books`(`id`) on delete cascade,
    foreign key (`category_id`) references `categories`(`id`) on delete cascade
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;