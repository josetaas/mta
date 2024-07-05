#!/bin/bash

# Set the classpath to include all JAR files in the lib directory
CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')

# Create the bin directory if it doesn't exist
mkdir -p bin

# Copy resources to the bin directory
cp -r resources/* bin/

# Compile the Java source files in the src directory
javac -cp "$CLASSPATH" -d bin src/MS1/*.java

# Check if the compilation succeeded
if [ $? -eq 0 ]; then
	echo "Compilation successful"
else
	echo "Compilation failed"
	exit 1
fi
