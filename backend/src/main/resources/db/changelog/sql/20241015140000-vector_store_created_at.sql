--liquibase formatted sql

--changeset andrea.rubino:20241015140000-1
--comment: Add created_at column
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='vector_store' and column_name='created_at';

ALTER TABLE vector_store
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
