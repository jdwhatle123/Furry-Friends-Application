@echo off
REM Run Furry Friends without Maven (requires Java 17+ installed)
setlocal

REM Resolve script dir and operate there (portable bundle friendly)
pushd %~dp0

REM Expect JavaFX and deps in lib\ alongside this script when bundled to Desktop
set JFX_MODS=javafx.controls,javafx.fxml,javafx.graphics,javafx.base
set MODULE_PATH=lib
set CP_FALLBACK=.;FurryFriends-1.0-SNAPSHOT.jar;lib\*

REM Prefer local JDK if JAVA_HOME set
set JAVA_BIN=java
if defined JAVA_HOME set JAVA_BIN="%JAVA_HOME%\bin\java"

REM Try modular launch first
"%JAVA_BIN%" --module-path %MODULE_PATH% --add-modules %JFX_MODS% -cp FurryFriends-1.0-SNAPSHOT.jar;lib\sqlite-jdbc-3.50.3.0.jar FurryFriendsApp
if errorlevel 1 (
  echo Module launch failed, falling back to classpath launch...
  "%JAVA_BIN%" -cp %CP_FALLBACK% FurryFriendsApp
)

popd
