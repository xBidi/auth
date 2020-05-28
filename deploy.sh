#!/usr/bin/env bash
docker-compose build
docker-compose down
docker-compose up -d
docker-compose logs -f --tail=100 spring >&1 | ccze -m ansi
