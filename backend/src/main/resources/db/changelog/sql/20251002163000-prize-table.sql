--liquibase formatted sql

--changeset andrea.rubino:20251301152000-1
--comment: Create prize_table
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from information_schema.columns where table_name='user_level' and column_name='username';

CREATE TABLE IF NOT EXISTS prize
(
    id        uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    prize_name       VARCHAR(50),
    position         INT,
    date    DATE
);

