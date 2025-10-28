@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0

call "%DIR%\mvnw.cmd" -q -DskipTests package || goto :eof
java -cp "%DIR%\target\project-1.0.jar" academy.Application %*


