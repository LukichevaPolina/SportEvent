# Dockerfile

FROM python:3.8

# RUN mkdir /sportevent
WORKDIR /sportevent

# disable write  files to *.pyc on disk and disable buffering stdout and stdenv
ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONBUFFERED=1

COPY . .

# COPY requirements.txt ./
# RUN pip install --no-cache-dir -r requirements.txt

RUN apk add --update --no-cache --virtual .tmp-build-deps \
        gcc libc-dev linux-headers postgresql-dev && \
    pip install --no-cache-dir -r requirements.txt