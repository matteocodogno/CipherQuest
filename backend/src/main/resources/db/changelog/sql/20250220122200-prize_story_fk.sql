--liquibase formatted sql

--changeset andrea.rubino:20250220120000-1
--comment: Create protected question foreign key to story
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='prize' and column_name='story_id';

ALTER TABLE prize
    ADD COLUMN IF NOT EXISTS story_id uuid REFERENCES story (id);

ALTER TABLE prize
    ADD CONSTRAINT fk_story_id FOREIGN KEY (story_id) REFERENCES story (id);
