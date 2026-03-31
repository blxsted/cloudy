#!/bin/bash

# Docker-Compose hochfahren (DB + App)
echo "Starte Docker-Container..."
docker-compose up -d

# Optional: Logs anzeigen
echo "Logs der Services:"
docker-compose logs -f