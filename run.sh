#!/bin/bash

CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')bin

java -cp "$CLASSPATH" TA.TA
