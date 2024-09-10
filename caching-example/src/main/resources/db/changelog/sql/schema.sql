CREATE SCHEMA my_schema;

CREATE TABLE my_schema.categories (
    id bigserial primary key,
    name character varying(255)
);

CREATE TABLE my_schema.books (
    id bigserial primary key,
    title character varying(255),
    author character varying(255),
    category_id bigint references my_schema.categories(id)
);


