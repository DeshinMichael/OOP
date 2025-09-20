#!/bin/bash

rm -rf build
rm -rf META-INF
rm -f application.jar

mkdir -p build/classes build/docs META-INF

echo -e "Manifest-Version: 1.0\nMain-Class: Main" > META-INF/MANIFEST.MF

javac src/main/java/*.java -d build/classes/java/main

javadoc -quiet src/main/java/*.java -d build/docs

jar cfm application.jar META-INF/MANIFEST.MF -C build/classes/java/main .

java -jar application.jar