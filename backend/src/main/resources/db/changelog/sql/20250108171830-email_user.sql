--liquibase formatted sql

--changeset andrea.rubino:20250108171830-1
--comment: Add column email
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='user_level' and column_name='email';

ALTER TABLE user_level
    ADD COLUMN email TEXT NULL UNIQUE;
