@echo off
(
    mvn clean
    mvn package -DskipTests
)
pause