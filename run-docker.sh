#!/usr/bin/env bash
set -euo pipefail

DIR="$(cd "$(dirname "$0")" && pwd)"

"$DIR"/mvnw -q -DskipTests package
docker build -t app "$DIR"
exec docker run --rm -u root -i app "$@"


