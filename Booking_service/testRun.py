import jwt
from app import app
import datetime

if __name__ == "__main__":
    app.run(debug=True,port=5002,host="0.0.0.0")
