import pandas as pd
import matplotlib.pyplot as plt
import os

# Automatically load the latest CSV file
EXPORT_DIR = "/app/python/exports"

def get_latest_csv(directory):
    files = [f for f in os.listdir(directory) if f.endswith(".csv")]
    if not files:
        raise FileNotFoundError("No CSV files found.")
    return sorted(files)[-1]  # Get most recent file

def plot_log_counts():
    filename = get_latest_csv(EXPORT_DIR)
    filepath = os.path.join(EXPORT_DIR, filename)
    print(f"Using file: {filepath}")

    df = pd.read_csv(filepath)

    # Count number of log entries per unique message (or just total if message is too detailed)
    message_counts = df["log_message"].value_counts()

    # Plotting
    plt.figure(figsize=(10, 5))
    message_counts.plot(kind="bar", color="skyblue")
    plt.title("Log Message Frequency")
    plt.xlabel("Log Message")
    plt.ylabel("Count")
    plt.xticks(rotation=45, ha="right")
    plt.tight_layout()

    output_img = filepath.replace(".csv", ".png")
    plt.savefig(output_img)
    print(f"Graph saved to: {output_img}")

if __name__ == "__main__":
    plot_log_counts()
