--liquibase formatted sql

--changeset andrea.rubino:20241205100000-1
--comment: Add info column
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='vector_store' and column_name='info';

ALTER TABLE vector_store
    ADD COLUMN info json not null default '{}'::jsonb;
