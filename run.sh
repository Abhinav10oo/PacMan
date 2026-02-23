#!/bin/bash
# Compile and run PacMan game

SRC_DIR="src"
BIN_DIR="bin"

echo "=== Compiling PacMan Game ==="
mkdir -p $BIN_DIR

# Find all .java files and compile
find $SRC_DIR -name "*.java" > sources.txt
javac -d $BIN_DIR @sources.txt

if [ $? -eq 0 ]; then
    echo "=== Compilation Successful! ==="
    rm sources.txt
    echo "=== Launching Game... ==="
    java -cp $BIN_DIR pacman.Main
else
    echo "=== Compilation FAILED ==="
    rm -f sources.txt
    exit 1
fi
