DROP TABLE IF EXISTS user_actions CASCADE;
DROP TABLE IF EXISTS events_similarity CASCADE;

CREATE TABLE IF NOT EXISTS user_actions (
    user_id BIGINT,
    event_id BIGINT,
    action_type VARCHAR(20),
    timestamp TIMESTAMPTZ,
    PRIMARY KEY (user_id, event_id)
);

CREATE TABLE IF NOT EXISTS events_similarity (
    eventa BIGINT,
    eventb BIGINT,
    score DOUBLE PRECISION,
    timestamp TIMESTAMPTZ,
    PRIMARY KEY (eventa, eventb)
);