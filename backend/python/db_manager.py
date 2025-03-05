import os
import psycopg2
import csv

def get_db_connection():
    conn = psycopg2.connect(
        host=os.environ.get('DB_HOST', 'localhost'),
        port=os.environ.get('DB_PORT', '5432'),
        database=os.environ.get('DB_NAME', 'mars_db'),
        user=os.environ.get('DB_USER', 'mars_user'),
        password=os.environ.get('DB_PASSWORD', 'supersecurepassword')
    )
    return conn

def insert_mission(name, objectives):
    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute("""
        INSERT INTO missions (name, start_time, status, objectives)
        VALUES (%s, NOW(), 'active', %s)
        RETURNING mission_id
    """, (name, objectives))
    mission_id = cur.fetchone()[0]
    conn.commit()
    cur.close()
    conn.close()
    return mission_id

def insert_log(mission_id, log_message):
    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute("""
        INSERT INTO logs (mission_id, log_message)
        VALUES (%s, %s)
    """, (mission_id, log_message))
    conn.commit()
    cur.close()
    conn.close()

def export_logs_to_csv(mission_id, file_path="mission_logs.csv"):
    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute("""
        SELECT log_id, timestamp, log_message
        FROM logs
        WHERE mission_id = %s
        ORDER BY timestamp ASC
    """, (mission_id,))
    rows = cur.fetchall()
    cur.close()
    conn.close()

    with open(file_path, mode='w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(["log_id", "timestamp", "log_message"])
        for log_id, timestamp, log_message in rows:
            writer.writerow([log_id, timestamp, log_message])
    print(f"Logs exported to {file_path}")
