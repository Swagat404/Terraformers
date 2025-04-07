import psycopg2
import csv
from datetime import datetime

def export_mission_logs_to_csv():
    try:
        connection = psycopg2.connect(
            host="db",  # updated from mars_db to match .env
            database="mars_db",
            user="mars_user",
            password="supersecurepassword"
        )
        cursor = connection.cursor()

        cursor.execute("SELECT * FROM logs;")
        rows = cursor.fetchall()
        headers = [desc[0] for desc in cursor.description]

        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"mission_logs_{timestamp}.csv"

        with open(f"/app/python/exports/{filename}", "w", newline="") as file:
            writer = csv.writer(file)
            writer.writerow(headers)
            writer.writerows(rows)

        print(f"CSV export successful: {filename}")
        return filename

    except Exception as e:
        print("Error exporting CSV:", e)

    finally:
        try:
            cursor.close()
            connection.close()
        except:
            pass

# For test/demo purposes
if __name__ == "__main__":
    export_mission_logs_to_csv()
