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
