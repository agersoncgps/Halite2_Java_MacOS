#!/bin/sh

javac DivideAndConquer.java
javac DoNothing.java
./halite -d "240 160" "java DivideAndConquer" "java DoNothing"