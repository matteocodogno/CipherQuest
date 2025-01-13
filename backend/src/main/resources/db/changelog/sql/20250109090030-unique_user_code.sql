--liquibase formatted sql

--changeset andrea.rubino:20250109090030-1
--comment: Add column unique_code
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='user_level' and column_name='unique_code';

ALTER TABLE user_level
    ADD COLUMN unique_code TEXT NULL UNIQUE;
