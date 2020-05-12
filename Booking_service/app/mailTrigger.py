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

app.config.update(mail_settings)
mail = Mail(app)


def sendEmail(recepients,subject,body,attachment):
    try:
        msg = Message(subject=subject,
                      sender=app.config.get("MAIL_USERNAME"),
                      recipients=[recepients],  # replace with your email for testing
                      body=body)
        if attachment is not None:
            fp = app.open_resource(attachment)
            pdfFile = fp.read()
            msg.attach("Ticket.pdf", "text/plain", pdfFile)
            fp.close()
        mail.send(msg)
    except Exception as e:
        app.logger.debug("Mail error")
        app.logger.debug(e)
