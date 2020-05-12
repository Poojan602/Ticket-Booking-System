FROM ubuntu:18.04

RUN apt-get update -y && \
    apt-get install -y python3-pip python-dev python3-cffi libcairo2 libpango-1.0-0 libpangocairo-1.0-0 libgdk-pixbuf2.0-0 libffi-dev shared-mime-info


COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

RUN pip3 --no-cache-dir install -r requirements.txt

COPY . /app

EXPOSE 5002

ENTRYPOINT [ "python3" ]

CMD [ "testRun.py" ]
