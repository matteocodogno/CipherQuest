--liquibase formatted sql

--changeset andrea.rubino:20240701133600-1
--comment: Create the user_level table

DROP TABLE IF EXISTS user_level;

CREATE TABLE IF NOT EXISTS user_level
(
    user_id       TEXT UNIQUE,
    username      VARCHAR(20)              NULL UNIQUE,
    level         INT                      DEFAULT 1,
    project       VARCHAR(50)              DEFAULT 'overmind',
    coins         INT                      DEFAULT 25,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    terminated_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE INDEX IF NOT EXISTS idx_user_id ON user_level (user_id);
