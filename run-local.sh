#!/usr/bin/env bash
set -euo pipefail

DIR="$(cd "$(dirname "$0")" && pwd)"

"$DIR"/mvnw -q -DskipTests package
exec java -cp "$DIR/target/project-1.0.jar" academy.Application "$@"


