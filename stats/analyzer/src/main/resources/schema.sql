CREATE TABLE IF NOT EXISTS user_actions (
    user_id BIGINT,
    event_id BIGINT,
    action_type VARCHAR(20),
    timestamp BIGINT,
    PRIMARY KEY (user_id, event_id)
);

CREATE TABLE IF NOT EXISTS event_similarities (
    event_id1 BIGINT,
    event_id2 BIGINT,
    similarity_score DOUBLE PRECISION,
    timestamp BIGINT,
    PRIMARY KEY (event_id1, event_id2)
);