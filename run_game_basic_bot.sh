#!/bin/sh

javac BasicBot.java
javac BasicBotOrigonal.java
./halite -d "240 160" "java BasicBot" "java BasicBotOrigonal"