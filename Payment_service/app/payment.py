from app import app
import logging
from flask import request
import json

logging.basicConfig(level=logging.DEBUG)

def getDataFromRequest(dataObj,keyValue,requestObj=None):
    if dataObj is not None:
        return dataObj.get(keyValue)
    else:
        return request.args.get(keyValue)

@app.route("/paymentHealth")
def payment_health():
    return "payment"


@app.route("/payment" , methods=['POST'])
def proceed_payment():
    response_json = {}
    response_json["message"] = "error"
    try:
        app.logger.debug(request)
        data = request.get_json()
        # app.logger.debug("Printing")
        app.logger.debug(data)
        card_number = getDataFromRequest(dataObj=data, keyValue="card_number")
        expiry = getDataFromRequest(dataObj=data, keyValue="expiry")
        cvv = getDataFromRequest(dataObj=data, keyValue="cvv")
        name = getDataFromRequest(dataObj=data, keyValue="name")

        # TODO: nothing is done here, this will act as a payment gateway
        response_json["message"] = "ok"


    except Exception as e:
        app.logger.debug(e)
        response_json["message"] = "error"

    return json.dumps(response_json)
