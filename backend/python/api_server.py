# api_server.py
from flask import Flask, request, jsonify
import psycopg2
import os
from datetime import datetime
import json

app = Flask(__name__)

# Database connection details
DB_HOST = os.environ.get('DB_HOST', 'db')
DB_PORT = os.environ.get('DB_PORT', '5432')
DB_NAME = os.environ.get('DB_NAME', 'mars_db')
DB_USER = os.environ.get('DB_USER', 'mars_user')
DB_PASSWORD = os.environ.get('DB_PASSWORD', 'supersecurepassword')

def get_db_connection():
    """Create a database connection"""
    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        database=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )
    conn.autocommit = True
    return conn

# API Endpoints
@app.route('/api/missions', methods=['POST'])
def create_mission():
    """Create a new mission and return the mission ID"""
    data = request.json
    name = data.get('name')
    objectives = data.get('objectives')
    
    if not name:
        return jsonify({"error": "Mission name is required"}), 400
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute(
            "INSERT INTO missions (name, start_time, status, objectives) VALUES (%s, %s, %s, %s) RETURNING mission_id",
            (name, datetime.now(), "ONGOING", objectives)
        )
        mission_id = cursor.fetchone()[0]
        return jsonify({"mission_id": mission_id, "status": "created"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/missions/<int:mission_id>', methods=['PUT'])
def update_mission(mission_id):
    """Update a mission's status and end time"""
    data = request.json
    status = data.get('status')
    
    if not status:
        return jsonify({"error": "Status is required"}), 400
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        # If mission is completed or failed, set end time
        if status in ["COMPLETED", "FAILED"]:
            cursor.execute(
                "UPDATE missions SET status = %s, end_time = %s WHERE mission_id = %s",
                (status, datetime.now(), mission_id)
            )
        else:
            cursor.execute(
                "UPDATE missions SET status = %s WHERE mission_id = %s",
                (status, mission_id)
            )
        
        if cursor.rowcount == 0:
            return jsonify({"error": "Mission not found"}), 404
        
        return jsonify({"mission_id": mission_id, "status": "updated"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/logs', methods=['POST'])
def create_log():
    """Create a new log entry"""
    data = request.json
    mission_id = data.get('mission_id')
    log_message = data.get('message')
    
    if not mission_id or not log_message:
        return jsonify({"error": "Mission ID and log message are required"}), 400
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute(
            "INSERT INTO logs (mission_id, timestamp, log_message) VALUES (%s, %s, %s) RETURNING log_id",
            (mission_id, datetime.now(), log_message)
        )
        log_id = cursor.fetchone()[0]
        return jsonify({"log_id": log_id, "status": "created"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/results', methods=['POST'])
def create_result():
    """Create a mission result"""
    data = request.json
    mission_id = data.get('mission_id')
    outcome = data.get('outcome')
    details = data.get('details')
    
    if not mission_id or not outcome:
        return jsonify({"error": "Mission ID and outcome are required"}), 400
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute(
            "INSERT INTO results (mission_id, outcome, details) VALUES (%s, %s, %s) RETURNING result_id",
            (mission_id, outcome, details)
        )
        result_id = cursor.fetchone()[0]
        
        # Update mission status to completed
        cursor.execute(
            "UPDATE missions SET status = 'COMPLETED', end_time = %s WHERE mission_id = %s",
            (datetime.now(), mission_id)
        )
        
        return jsonify({"result_id": result_id, "status": "created"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/missions/<int:mission_id>/logs', methods=['GET'])
def get_mission_logs(mission_id):
    """Get all logs for a specific mission"""
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute(
            "SELECT log_id, timestamp, log_message FROM logs WHERE mission_id = %s ORDER BY timestamp",
            (mission_id,)
        )
        logs = []
        for log in cursor.fetchall():
            logs.append({
                "log_id": log[0],
                "timestamp": log[1].isoformat(),
                "message": log[2]
            })
        
        return jsonify({"mission_id": mission_id, "logs": logs}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/brain/<string:brain_type>', methods=['GET'])
def get_brain_parameters(brain_type):
    """Get AI brain parameters based on brain type"""
    # In a real implementation, these could be stored in the database
    brain_parameters = {
        "fast": {
            "moveSpeed": 0.05,
            "jumpForce": 0.02,
            "gravity": -9.81,
            "runSpeed": 0.1,
            "climbSpeed": 0.03,
            "brainType": "Fast"
        },
        "bouncy": {
            "moveSpeed": 0.03,
            "jumpForce": 0.05,
            "gravity": -7.0,
            "runSpeed": 0.06,
            "climbSpeed": 0.02,
            "brainType": "Bouncy"
        },
        "flying": {
            "moveSpeed": 0.04,
            "jumpForce": 0.01,
            "gravity": -4.0,
            "runSpeed": 0.08,
            "climbSpeed": 0.01,
            "canFly": True,
            "brainType": "Flying"
        }
    }
    
    if brain_type.lower() not in brain_parameters:
        return jsonify({"error": "Brain type not found"}), 404
    
    return jsonify(brain_parameters[brain_type.lower()]), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)