@echo off
echo === Compiling PacMan Game ===
if not exist bin mkdir bin

dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt

if %errorlevel% == 0 (
    echo === Compilation Successful! ===
    del sources.txt
    echo === Launching Game... ===
    java -cp bin pacman.Main
) else (
    echo === Compilation FAILED ===
    del sources.txt
)
pause
