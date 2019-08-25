#!/bin/sh -e

./mvnw clean verify

tee target/input.txt <<EOF
Jessie is not the best developer
Evan is not the worst developer
John is not the best developer or the worst developer
Sarah is a better developer than Evan
Matt is not directly below or above John as a developer
John is not directly below or above Evan as a developer
EOF

java -jar target/find-10x-developer.jar "$(pwd)/target/input.txt"