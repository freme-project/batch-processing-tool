#!/bin/bash

#e-Translation
java -jar ../../../../target/batch-processing-tool-0.1-SNAPSHOT-jar-with-dependencies.jar -f text -if ./input.txt -o turtle -s en -t de e-translate

#e-Entity
java -jar ../../../../target/batch-processing-tool-0.1-SNAPSHOT-jar-with-dependencies.jar -f text -if ./input2.txt -o turtle e-entity