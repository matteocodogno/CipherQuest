--liquibase formatted sql

--changeset andrea.rubino:20250220115000-1
--comment: Create story table
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name='story';

CREATE TABLE IF NOT EXISTS story
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    name      VARCHAR(50) unique NOT NULL
);

