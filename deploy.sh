#!/usr/bin/env bash
docker volume create db-data
docker-compose build
docker-compose down
docker-compose up -d
docker-compose logs -f 2>&1 | ccze -m ansi
