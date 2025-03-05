-- Missions Table
CREATE TABLE IF NOT EXISTS missions (
    mission_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(50),
    objectives TEXT
);

-- Logs Table
CREATE TABLE IF NOT EXISTS logs (
    log_id SERIAL PRIMARY KEY,
    mission_id INT REFERENCES missions(mission_id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    log_message TEXT
);

-- Results Table
CREATE TABLE IF NOT EXISTS results (
    result_id SERIAL PRIMARY KEY,
    mission_id INT REFERENCES missions(mission_id),
    outcome VARCHAR(50),
    details TEXT
);
