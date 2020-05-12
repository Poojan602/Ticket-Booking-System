from flask_mail import Mail, Message
from app import app
import logging

mail_settings = {
    "MAIL_SERVER": 'smtp.gmail.com',
    "MAIL_PORT": 465,
    "MAIL_USE_TLS": False,
    "MAIL_USE_SSL": True,
    "MAIL_USERNAME": "t.karthikk10@gmail.com",
    "MAIL_PASSWORD": "fjooivhzawknromj"
}
logging.basicConfig(level=logging.DEBUG)

#https://www.geeksforgeeks.org/python-convert-html-pdf/

app.config.update(mail_settings)
mail = Mail(app)

def sendEmail(recepients,subject,body,attachmentName=None):
    try:
        msg = Message(subject=subject,
                      sender=app.config.get("MAIL_USERNAME"),
                      recipients=[recepients]  # replace with your email for testing
                      )
        msg.html = body
        if attachmentName is not None:
            fileObj = open("example.png",mode='r')
            msg.attachments(filename="example.png",data=fileObj.read())
        mail.send(msg)
    except Exception as e:
        app.logger.debug("Error sending email =======>")
        app.logger.debug(e)
