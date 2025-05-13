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
) engine =InnoDB default charset=utf8mb4 collate =utf8mb4_unicode_ci;