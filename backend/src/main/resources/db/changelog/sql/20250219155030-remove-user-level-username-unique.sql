--liquibase formatted sql

--changeset andrea.rubino:20250219155030-1
--comment: Remove username unique index

ALTER TABLE user_level DROP CONSTRAINT user_level_username_key;
