@echo off
title Milestone1

javac -sourcepath ../../Components/Milestone1 -cp ../../Components/* ../../Components/Milestone1/*.java
start "Milestone1" /D"../../Components/Milestone1" java -cp .;../* CreateTempBloodPressure