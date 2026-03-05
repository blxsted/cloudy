#!/bin/bash

# Prüfen, ob der Container schon existiert
if [ "$(docker ps -aq -f name=postgres-dev)" ]; then
    echo "Container exists, starting..."
    docker start postgres-dev
else
    echo "Container does not exist, creating..."
    docker run --name postgres-dev \
      -e POSTGRES_DB=demo \
      -e POSTGRES_USER=demo \
      -e POSTGRES_PASSWORD=demo \
      -p 5433:5432 \
      -d postgres:16
fi