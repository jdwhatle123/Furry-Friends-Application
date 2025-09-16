# Runs the Furry Friends JavaFX app from the project root
param(
  [switch]$Clean
)

$ErrorActionPreference = 'Stop'

# Move to the script directory, then project root
Set-Location -Path (Split-Path -Parent $MyInvocation.MyCommand.Path)
Set-Location -Path ..

if ($Clean) { mvn -q -DskipTests=true clean }

# Build (incremental) then run
mvn -q -DskipTests=true compile
mvn -DskipTests=true javafx:run
