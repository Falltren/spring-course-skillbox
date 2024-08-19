CREATE TABLE users (
    id bigserial primary key,
    name character varying(255),
    email character varying(255),
    password character varying(255)
);

CREATE TABLE categories (
    id bigserial primary key,
    title character varying(255)
);

CREATE TABLE news (
    id bigserial primary key,
    title character varying(255),
    text character varying(255),
    create_at timestamp(6) with time zone,
    update_at timestamp(6) with time zone,
    category_id bigint references categories(id),
    user_id bigint references users(id)
);

CREATE TABLE comments (
    id bigserial primary key,
    text character varying(255),
    create_at timestamp(6) with time zone,
    update_at timestamp(6) with time zone,
    news_id bigint references news(id)
);