#!/bin/sh -e

GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m'

if ! hash docker; then
    echo "docker not found: please install docker and run again"
    exit 1
fi

docker pull monkeycodes/find-10x-developer:1.0


echo "${CYAN}"
cat src/test/resources/input.txt
echo "${GREEN}"
docker run --rm -v $(pwd)/src/test/resources/input.txt:/input.txt monkeycodes/find-10x-developer:1.0 input.txt
echo "${NC}"