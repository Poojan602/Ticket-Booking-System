from testRun import app
from flask import json
import os
import unittest

class BasicTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True
        pass

    def test_bookTicketFailure(self):
        response = self.app.post('/bookticket',json={'email': 'dGVzdEB0ZXN0LmNvbQ==', 'date': 'MTEtMDQtMjAyMA==', 'price':'MTAw',
        'from':'aGFsaWZheA==','to':'Y2FsZ2FyeQ==','name':'U29oYWls','payment_info':{'card_number':'MjIyMjIyMjIyMjIyMjIyMg==','expiry':'MDcvMjM=','cvv':'NDU2'}},
        content_type='application/json',)
        data = json.loads(response.get_data(as_text=True))
        assert data['message'] == 'error'

    def test_cardDetailsFailing(self):
        p = {('email','dGVzdEB0ZXN0LmNvbQ==')}
        response = self.app.get('/bookticket/carddetails',query_string=p)
        data = json.loads(response.get_data(as_text=True))
        assert data['message'] == 'error'



if __name__ == "__main__":
    unittest.main()
