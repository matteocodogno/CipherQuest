--changeset andrea.rubino:20240701133600
--comment: Create the user_level table

CREATE TABLE IF NOT EXISTS user_level
(
    user_id text UNIQUE,
    level   int default 1
);

CREATE INDEX IF NOT EXISTS idx_user_id ON user_level (user_id);


