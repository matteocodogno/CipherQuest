--liquibase formatted sql

--changeset andrea.rubino:202410211600-1
--comment: Add column question_counter
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='user_level' and column_name='question_counter';

ALTER TABLE user_level
    ADD COLUMN question_counter INT DEFAULT 0;

