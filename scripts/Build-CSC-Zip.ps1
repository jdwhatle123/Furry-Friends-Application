param(
    [string]$ZipName = "doFurryFriends-CSC.zip"
)

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$proj = Split-Path -Parent $root

$docs = Join-Path $proj 'docs'
$diagrams = Join-Path $docs 'Diagrams'

# Expected files
$docx1 = Join-Path $proj 'CSC-Section.docx'
$docx2 = Join-Path $proj 'Full-Design-Document.docx'
$pngs = @(
    'Figure5.png','Figure5a.png','Figure5b.png','Figure5c.png',
    'LogicalView.png','UserView.png','ProcessView-Activity.png','ProcessView-Sequence.png','ProcessView-Collaboration.png','ProcessView-Dispatch.png','PhysicalView.png'
) | ForEach-Object { Join-Path $diagrams $_ }

# Validate presence (warn only)
$missing = @()
foreach ($p in @($docx1,$docx2) + $pngs) { if (-not (Test-Path $p)) { $missing += $p } }
if ($missing.Count -gt 0) {
    Write-Warning "Missing files (will still zip existing ones):"
    $missing | ForEach-Object { Write-Warning " - $_" }
}

# Build zip
$tmp = Join-Path $env:TEMP ([System.IO.Path]::GetRandomFileName())
New-Item -ItemType Directory -Path $tmp | Out-Null

# Copy docs
if (Test-Path $docx1) { Copy-Item $docx1 -Destination (Join-Path $tmp 'CSC-Section.docx') }
if (Test-Path $docx2) { Copy-Item $docx2 -Destination (Join-Path $tmp 'Full-Design-Document.docx') }

# Copy diagrams
$dz = Join-Path $tmp 'Diagrams'
New-Item -ItemType Directory -Path $dz | Out-Null
foreach ($p in $pngs) { if (Test-Path $p) { Copy-Item $p -Destination $dz } }

# Create zip at project root
$zipPath = Join-Path $proj $ZipName
if (Test-Path $zipPath) { Remove-Item $zipPath }
Compress-Archive -Path (Join-Path $tmp '*') -DestinationPath $zipPath -Force

Write-Host "Created $zipPath"
