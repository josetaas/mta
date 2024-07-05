#!/bin/bash

# Set the classpath to include all JAR files in the lib directory and the bin directory
CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')bin

# Run the main class (replace MS1.Milestone1 with your main class)
java -cp "$CLASSPATH" MS1.Milestone1
