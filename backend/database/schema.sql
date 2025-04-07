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

-- Conceptual SQL for terrains table
CREATE TABLE terrains (
    terrain_id INT PRIMARY KEY, -- Or SERIAL if using PostgreSQL auto-increment
    terrain_type VARCHAR(50),   -- Store enum name as string
    surface_hardness REAL,     -- Or FLOAT, DECIMAL depending on precision needed
    description VARCHAR(255)
);

-- Database: mydatabase (Example name)

-- 1. terrains table
CREATE TABLE terrains (
    terrain_id INTEGER PRIMARY KEY,      -- Use SERIAL PRIMARY KEY for auto-increment
    terrain_type VARCHAR(50),
    surface_hardness REAL,
    description VARCHAR(255)
);

-- 2. missions table
CREATE TABLE missions (
    mission_id INTEGER PRIMARY KEY,      -- Use SERIAL PRIMARY KEY for auto-increment
    mission_name VARCHAR(255),
    status VARCHAR(20),
    objective VARCHAR(1000)
);

-- 3. avatar_brains table
CREATE TABLE avatar_brains (
    avatar_brain_id INTEGER PRIMARY KEY, -- Use SERIAL PRIMARY KEY for auto-increment
    avatar_brain_type VARCHAR(20),
    avatar_speed INTEGER,
    avatar_max_jump_height INTEGER,
    avatar_length REAL,
    avatar_width REAL,
    avatar_height REAL
    -- Foreign key to avatars table will be added IN the avatars table (for OneToOne)
);

-- 4. locations table (depends on terrains)
CREATE TABLE locations (
    location_id INTEGER PRIMARY KEY,     -- Use SERIAL PRIMARY KEY for auto-increment
    longitude REAL,
    latitude REAL,
    altitude REAL,
    slope REAL,
    terrain_id INTEGER NOT NULL,         -- Foreign Key column
    CONSTRAINT fk_location_terrain FOREIGN KEY (terrain_id) REFERENCES terrains(terrain_id)
    -- Foreign key to avatars table will be added IN the avatars table (for OneToOne)
);

-- 5. avatars table (depends on locations, avatar_brains, missions)
CREATE TABLE avatars (
    avatar_id INTEGER PRIMARY KEY,       -- Use SERIAL PRIMARY KEY for auto-increment
    avatar_name VARCHAR(100),
    avatar_color VARCHAR(20),
    location_id INTEGER UNIQUE,          -- Foreign Key for OneToOne with Location (can be nullable if optional)
    avatar_brain_id INTEGER UNIQUE,      -- Foreign Key for OneToOne with AvatarBrain (can be nullable if optional)
    mission_id INTEGER,                  -- Foreign Key for ManyToOne with Mission (can be nullable if optional)
    CONSTRAINT fk_avatar_location FOREIGN KEY (location_id) REFERENCES locations(location_id),
    CONSTRAINT fk_avatar_brain FOREIGN KEY (avatar_brain_id) REFERENCES avatar_brains(avatar_brain_id),
    CONSTRAINT fk_avatar_mission FOREIGN KEY (mission_id) REFERENCES missions(mission_id)
);

-- 6. motors table (depends on avatar_brains)
CREATE TABLE motors (
    motor_id INTEGER PRIMARY KEY,        -- Use SERIAL PRIMARY KEY for auto-increment
    max_speed REAL,
    power_consumption REAL,
    position VARCHAR(10),
    status VARCHAR(20),
    motor_type VARCHAR(10),
    avatar_brain_id INTEGER NOT NULL,    -- Foreign Key column
    CONSTRAINT fk_motor_brain FOREIGN KEY (avatar_brain_id) REFERENCES avatar_brains(avatar_brain_id)
);

-- 7. sensors table (depends on avatars)
CREATE TABLE sensors (
    sensor_id INTEGER PRIMARY KEY,       -- Use SERIAL PRIMARY KEY for auto-increment
    mount_position VARCHAR(10),
    status VARCHAR(20),
    sensor_type VARCHAR(10),
    avatar_id INTEGER NOT NULL,          -- Foreign Key column
    CONSTRAINT fk_sensor_avatar FOREIGN KEY (avatar_id) REFERENCES avatars(avatar_id)
);

-- 8. avatar_logs table (depends on avatars)
-- Includes columns inherited from Log mapped superclass
CREATE TABLE avatar_logs (
    log_id INTEGER PRIMARY KEY,          -- Use SERIAL PRIMARY KEY for auto-increment
    log_date DATE,
    log_time TIME,
    log_type VARCHAR(20),
    log_message VARCHAR(1000),
    avatar_id INTEGER NOT NULL,          -- Foreign Key column
    CONSTRAINT fk_avatarlog_avatar FOREIGN KEY (avatar_id) REFERENCES avatars(avatar_id)
);

-- 9. mission_logs table (depends on missions)
-- Includes columns inherited from Log mapped superclass
CREATE TABLE mission_logs (
    log_id INTEGER PRIMARY KEY,          -- Use SERIAL PRIMARY KEY for auto-increment
    log_date DATE,
    log_time TIME,
    log_type VARCHAR(20),
    log_message VARCHAR(1000),
    mission_id INTEGER NOT NULL,         -- Foreign Key column
    CONSTRAINT fk_missionlog_mission FOREIGN KEY (mission_id) REFERENCES missions(mission_id)
);

-- 10. motor_readings table (depends on motors)
-- Includes columns inherited from Reading mapped superclass
CREATE TABLE motor_readings (
    reading_id INTEGER PRIMARY KEY,      -- Use SERIAL PRIMARY KEY for auto-increment
    time_stamp TIMESTAMP,                -- Maps LocalDateTime
    current_speed REAL,
    direction VARCHAR(50),
    current_power REAL,
    motor_id INTEGER NOT NULL,           -- Foreign Key column
    CONSTRAINT fk_motorreading_motor FOREIGN KEY (motor_id) REFERENCES motors(motor_id)
);

-- 11. sensor_readings table (depends on sensors)
-- Includes columns inherited from Reading mapped superclass
CREATE TABLE sensor_readings (
    reading_id INTEGER PRIMARY KEY,      -- Use SERIAL PRIMARY KEY for auto-increment
    time_stamp TIMESTAMP,                -- Maps LocalDateTime
    value REAL,
    sensor_id INTEGER NOT NULL,          -- Foreign Key column
    CONSTRAINT fk_sensorreading_sensor FOREIGN KEY (sensor_id) REFERENCES sensors(sensor_id)
);