@echo off
REM Runs the Furry Friends JavaFX app from the project root
pushd %~dp0
cd ..

REM Build (incremental) then run
mvn -q -DskipTests=true compile
mvn -DskipTests=true javafx:run

popd
