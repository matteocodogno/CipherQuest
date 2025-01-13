--liquibase formatted sql

--changeset andrea.rubino:202513011520-1
--comment: Make username not limited into user_level table
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from information_schema.columns where table_name='user_level' and column_name='username';

ALTER TABLE user_level ALTER COLUMN username TYPE TEXT;

