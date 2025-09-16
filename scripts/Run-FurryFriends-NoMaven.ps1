<#
 Run Furry Friends without Maven (requires Java 17+ installed)
 Designed to run from a portable bundle where this script sits next to:
  - FurryFriends-1.0-SNAPSHOT.jar
  - lib/ (javafx.*.jar + sqlite-jdbc-3.50.3.0.jar)
#>
$ErrorActionPreference = 'Stop'

# Move to the folder where this script is located (do not go up a level)
Set-Location -Path $PSScriptRoot

$jfxMods = "javafx.controls,javafx.fxml,javafx.graphics,javafx.base"
# Build a module-path from platform-specific JavaFX jars if available
$winJfxJars = Get-ChildItem -Path "lib" -Filter "javafx-*-win.jar" -ErrorAction SilentlyContinue |
  Sort-Object Name | ForEach-Object { $_.FullName }
$modulePath = if ($winJfxJars -and $winJfxJars.Count -gt 0) { [string]::Join(';', $winJfxJars) } else { 'lib' }
$cpFallback = ".;FurryFriends-1.0-SNAPSHOT.jar;lib/*"

function Start-AppWithModules {
  param([string]$java)
  & $java --module-path $modulePath --add-modules $jfxMods -cp "FurryFriends-1.0-SNAPSHOT.jar;lib/sqlite-jdbc-3.50.3.0.jar" FurryFriendsApp
}

function Start-AppWithClasspath {
  param([string]$java)
  & $java -cp $cpFallback FurryFriendsApp
}

if ($env:JAVA_HOME) {
  Start-AppWithModules "$env:JAVA_HOME/bin/java"
} else {
  Start-AppWithModules "java"
}

if ($LASTEXITCODE -ne 0) {
  Write-Warning "Module launch failed (exit $LASTEXITCODE), falling back to classpath (non-modular) launch..."
  if ($env:JAVA_HOME) {
    Start-AppWithClasspath "$env:JAVA_HOME/bin/java"
  } else {
    Start-AppWithClasspath "java"
  }
}
