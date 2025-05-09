--liquibase formatted sql

--changeset sara.mangialavori:20250423143810-1
--comment: Create fixed_bot_message table

CREATE TYPE message_type AS ENUM
(
    'WIN',
    'GAME_OVER',
    'CHEAT_DETECT',
    'PROTECTED',
    'DOCUMENT'
);

CREATE TABLE IF NOT EXISTS fixed_bot_message
(
    id         uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    type       message_type,
    message    TEXT,
    story_name TEXT
);

--changeset sara.mangialavori:20250423143810-2
--comment: Add unique constraint to prevent duplicate type-story_name combinations

ALTER TABLE fixed_bot_message
    ADD CONSTRAINT unique_type_story UNIQUE (type, story_name);