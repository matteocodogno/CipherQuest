--liquibase formatted sql

--changeset sara.mangialavori:20250213154430-1
--comment: Create protected_question table

CREATE TABLE IF NOT EXISTS protected_question
(
    id          uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    question    TEXT,
    story_name  TEXT
);

--changeset sara.mangialavori:20250213154430-2
--comment: Replace protected_question table with one compliant with the vector extension

DROP TABLE IF EXISTS protected_question;

CREATE TABLE protected_question
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content   text,
    metadata  json,
    embedding vector(1536)
);

CREATE INDEX ON protected_question USING HNSW (embedding vector_cosine_ops);

