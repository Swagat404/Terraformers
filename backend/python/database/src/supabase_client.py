import os
from supabase import create_client
from dotenv import load_dotenv

def get_supabase_client():
    """Get a connection to Supabase via REST API"""
    load_dotenv()
    supabase_url = os.environ.get('SUPABASE_URL')
    supabase_key = os.environ.get('SUPABASE_KEY')
    
    if not supabase_url or not supabase_key:
        raise ValueError("Supabase URL and key must be set in environment variables")
    return create_client(supabase_url, supabase_key)

