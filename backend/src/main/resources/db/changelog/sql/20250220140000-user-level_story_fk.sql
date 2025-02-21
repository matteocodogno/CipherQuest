--liquibase formatted sql

--changeset andrea.rubino:20250220120000-1
--comment: Create user_level foreign key to story
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='user_level' and column_name='story_id';

ALTER TABLE user_level
    ADD COLUMN IF NOT EXISTS story_id uuid REFERENCES story (id);

ALTER TABLE user_level
    ADD CONSTRAINT fk_story_id FOREIGN KEY (story_id) REFERENCES story (id);
