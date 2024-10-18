--liquibase formatted sql

--changeset edoardo.paronzini:20241017083000-1
--comment: Add column updated_at
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='user_level' and column_name='updated_at';

ALTER TABLE user_level
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;

--changeset edoardo.paronzini:20241017083000-2
--comment: Add column score
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where table_name='user_level' and column_name='score';

ALTER TABLE user_level
    ADD COLUMN score BIGINT DEFAULT 0;

