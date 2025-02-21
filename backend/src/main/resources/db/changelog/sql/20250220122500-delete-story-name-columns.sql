--liquibase formatted sql

--changeset andrea.rubino:20250220140000-1
--comment: Drop story_name columns

ALTER TABLE level_up_question
    DROP COLUMN story_name;

ALTER TABLE prize
    DROP COLUMN story_name;

ALTER TABLE protected_question
    DROP COLUMN story_name;

ALTER TABLE user_level
    DROP COLUMN project;
