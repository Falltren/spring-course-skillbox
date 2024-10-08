CREATE TABLE user_roles (
    user_id bigint references users(id),
    roles character varying(255),
    PRIMARY KEY (user_id, roles)
);