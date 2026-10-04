#!/usr/bin/env bash
set -eu

PROJECT_DIR=/home/ubuntu/LMS-MICROSERVICE
COMPOSE=(docker compose --env-file deploy/.env -f compose.dev.yml)

cd "$PROJECT_DIR"

case "${1:-}" in
  start)
    "${COMPOSE[@]}" up -d
    ;;
  stop)
    "${COMPOSE[@]}" down
    ;;
  *)
    echo "Usage: $0 {start|stop}" >&2
    exit 2
    ;;
esac
