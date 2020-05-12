from flask import Flask
from flask_cors import CORS
import os
from flasgger import Swagger

app = Flask(__name__)
CORS(app)

template = {
  "swagger": "2.0",
  "info": {
    "title": "User Service APIs",
    "description": "API to create, login and verify user in Tourism App",
    "version": "1.0.1"
  },
  "schemes": [
    "http",
  ],
}

### swagger specific ###
swagger = Swagger(app , template=template)

app.config.update(
    SECRET_KEY=os.environ.get("SECRET_KEY"),
    AWS_ACCESS_KEY_ID=os.environ.get("AWS_ACCESS_KEY_ID"),
    AWS_SECRET_ACCESS_KEY=os.environ.get("AWS_SECRET_ACCESS_KEY"),
    AWS_SESSION_TOKEN=os.environ.get("AWS_SESSION_TOKEN")
    #SECRET_KEY=b'_5#y2L"F4Q8z\n\xec]/'\
)

from app import routes

