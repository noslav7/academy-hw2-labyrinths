@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0

call "%DIR%\mvnw.cmd" -q -DskipTests package || goto :eof
docker build -t app "%DIR%" || goto :eof
docker run --rm -u root -i app %*


