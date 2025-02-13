--liquibase formatted sql

--changeset sara.mangialavori:20250213154430-1
--comment: Create protected_question table

CREATE TABLE IF NOT EXISTS protected_question
(
    id          uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    question    TEXT,
    story_name  TEXT
);
