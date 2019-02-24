@echo off
title Milestone1

javac -sourcepath ../../Components/Milestone1 -cp ../../Components/* ../../Components/Milestone1/*.java
start "TempBloodPressure" /D"../../Components/TempBloodPressure" java -cp .;../* CreateTempBloodPressure