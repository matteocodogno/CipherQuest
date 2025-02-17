--liquibase formatted sql

--changeset andrea.rubino:20250217095500-1
--comment: Add column story_name
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='prize' and column_name='story_name';

ALTER TABLE prize
    ADD COLUMN story_name TEXT NULL;

