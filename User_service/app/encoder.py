import jwt
from app import app
import datetime
import logging

logging.basicConfig(level=logging.DEBUG)

def encode_auth_token(user_id):
    """
    Generates the Auth Token
    :return: string
    """
    try:
        payload = {
            'exp': datetime.datetime.utcnow() + datetime.timedelta(days=0, seconds=6000),
            'iat': datetime.datetime.utcnow(),
            'sub': user_id
        }

        # return payload
        # return "sss"
        # return "sssss"
        # return app.config.get('SECRET_KEY')
        app.logger.info('Processing default request')
        token = jwt.encode(
            payload,
            app.config.get('SECRET_KEY'),
            algorithm='HS256'
        )
        app.logger.debug(token)
        return token
    except Exception as e:
        return e

def decode_auth_token(auth_token):
    """
    Decodes the auth token
    :param auth_token:
    :return: integer|string
    """
    try:
        payload = jwt.decode(auth_token, app.config.get('SECRET_KEY'))
        return payload['sub']
    except jwt.ExpiredSignatureError:
        return 'Signature expired. Please log in again.'
    except jwt.InvalidTokenError:
        return 'Invalid token. Please log in again.'

def check_validity_token(auth_token,email):
    if email == decode_auth_token(auth_token):
        return True
    return False
