--liquibase formatted sql

--changeset edoardo.paronzini:20241010101400-1
--comment: Make username not nullable into user_level table
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from information_schema.columns where table_name='user_level' and column_name='username';

ALTER TABLE user_level ALTER COLUMN username SET NOT NULL;

