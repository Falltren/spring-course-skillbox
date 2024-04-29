CREATE SCHEMA IF NOT EXISTS my_schema;

CREATE TABLE my_schema.users (
    id bigserial primary key,
    name character varying(255),
    email character varying(255)
);

CREATE TABLE my_schema.categories (
    id bigserial primary key,
    title character varying(255)
);

CREATE TABLE my_schema.news (
    id bigserial primary key,
    title character varying(255),
    text character varying(255),
    date timestamp(6) without time zone,
    category_id bigint references my_schema.categories(id),
    user_id bigint references my_schema.users(id)
);

CREATE TABLE my_schema.comments (
    id bigserial primary key,
    text character varying(255),
    date timestamp(6) without time zone,
    news_id bigint references my_schema.news(id)
);