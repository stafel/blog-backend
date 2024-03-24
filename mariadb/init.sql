create database if not exists blog_backend;
create user if not exists backend@blog_backend identified by 'YourPassHere';
GRANT ALL PRIVILEGES ON blog_backend.* To backend;