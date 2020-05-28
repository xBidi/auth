#!/usr/bin/env bash
docker-compose build
docker-compose down
docker-compose up -d
docker-compose logs -f 2>&1 | ccze -m ansi
