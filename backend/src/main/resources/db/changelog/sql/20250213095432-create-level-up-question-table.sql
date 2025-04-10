--liquibase formatted sql

--changeset sara.mangialavori:20250213095432-1
--comment: Create level_up_question table

CREATE TABLE IF NOT EXISTS level_up_question
(
    id          uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    level       INT,
    question    TEXT,
    story_name  TEXT
);

--changeset sara.mangialavori:20250213095432-2
--comment: Replace level_up_question table with one compliant with the vector extension

DROP TABLE IF EXISTS level_up_question;

CREATE TABLE level_up_question
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content   text,
    metadata  json,
    embedding vector(1536)
);

CREATE INDEX ON level_up_question USING HNSW (embedding vector_cosine_ops);
