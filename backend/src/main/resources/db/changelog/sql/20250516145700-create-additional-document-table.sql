--liquibase formatted sql

--changeset sara.mangialavori:20250516145700-1
--comment: Create additional_document table

CREATE TABLE IF NOT EXISTS additional_document
(
    id         uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    type       TEXT,
    source     TEXT,
    content    TEXT,
    level      INT,
    story_name TEXT,
    UNIQUE (source, story_name)
);