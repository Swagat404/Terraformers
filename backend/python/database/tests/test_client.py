import os
import sys

# Add the path to the `supabase_client` module
#sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../../src/database')))
#print("sys.path:", sys.path)  # Debugging: Print sys.path to check the paths
from database.src.supabase_client import get_supabase_client

def simple_test():
    try:
        # Get Supabase client
        print("🔄 Getting Supabase client...")
        client = get_supabase_client()
        print(f"✅ Successfully created Supabase client for {os.environ.get('SUPABASE_URL')}")
        
        # Try a simple query
        print("🔄 Testing a simple query...")
        
        # If you know a table that exists, use it here
        table_name = "avatar_brain"  # Change this to your actual table name
        
        try:
            response = client.table(table_name).select("*").limit(5).execute()
            print(f"✅ Query successful! Found {len(response.data)} records")
            
            if response.data:
                print(f"📊 Sample data from first record:")
                for key, value in response.data[0].items():
                    print(f"  {key}: {value}")
    
        except Exception as e:
            print(f"⚠️ Query failed: {e}")
            print("⚠️ If this is because the table doesn't exist, try changing the table name")
        
    except ValueError as e:
        print(f"❌ Configuration error: {e}")
    except Exception as e:
        print(f"❌ Connection error: {e}")

if __name__ == "__main__":
    print("Running simple Supabase client test...")
    simple_test()
