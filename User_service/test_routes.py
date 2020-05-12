from testRun import app
from flask import json
import os
import unittest
import base64


class BasicTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True
        pass

    def test_createUserInvalidEmail(self):
        response = self.app.post('/user/create',
        json={'name': 'dGVzdAo=', 'email': 'YmxhYmxhYmxh', 'password': 'aGVsbG8='},
        content_type='application/json',)

        data = json.loads(response.get_data(as_text=True))
        assert data['message'] == 'error'
        assert data['error'] == 'email id given is either not reachable or invalid'

    def test_createUserSuccessTest(self):
        response = self.app.post('/user/create',
        json={'name': 'dGVzdAo=', 'email': 'dGVzdGNhc2VzQHRlc3QuY29t', 'password': 'aGVsbG8='},
        content_type='application/json',)

        data = json.loads(response.get_data(as_text=True))
        assert data['message'] == 'ok'

    def test_loginFailTest(self):
        p = (('email','YmxhYmxhYmxh'),('password','aGVsbG8='))
        response = self.app.get('/user/login',query_string=p)
        data = json.loads(response.get_data(as_text=True))
        assert data['message'] == 'error'

    def test_loginSuccessTest(self):
        p = (('email','dGVzdGNhc2VzQHRlc3QuY29t'),('password','aGVsbG8='))
        response = self.app.get('/user/login',query_string=p)
        data = json.loads(response.get_data(as_text=True))
        assert data['message'] == 'ok'
        

if __name__ == "__main__":
    unittest.main()
