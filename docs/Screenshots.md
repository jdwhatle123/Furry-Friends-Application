# Furry Friends — Screenshot Gallery

Below are the screenshots to include in your presentation and report. Run the copy script first to stage images into `docs/images/`, then open this file to verify inline previews.

## Quick copy script (PowerShell)
```powershell
# Create images folder if missing
$dst = "docs/images"; if (-not (Test-Path $dst)) { New-Item -ItemType Directory -Path $dst | Out-Null }

# List of source paths (edit as needed)
$srcs = @(
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 220520.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 112740.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110350.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110328.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110245.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110207.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110200.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110131.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110107.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110032.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110015.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 110002.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105951.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105940.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105930.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105910.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105901.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105852.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-16 105834.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 231113.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 230529.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 225932.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 225430.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 222729.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 222716.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 220509.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 220431.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 220415.png",
  "C:\Users\jdwha\OneDrive\Pictures\Screenshots\Screenshot 2025-09-15 215453.png"
)

foreach ($s in $srcs) {
  if (Test-Path $s) {
    Copy-Item -Path $s -Destination (Join-Path $dst ([System.IO.Path]::GetFileName($s))) -Force
  } else {
    Write-Warning "Missing: $s"
  }
}
```

## Gallery (after copying)

<!-- Example entries; VS Code/MkDocs/GitHub will inline these -->

![Screenshot 2025-09-15 220520](images/Screenshot%202025-09-15%20220520.png)
![Screenshot 2025-09-16 112740](images/Screenshot%202025-09-16%20112740.png)
![Screenshot 2025-09-16 110350](images/Screenshot%202025-09-16%20110350.png)
![Screenshot 2025-09-16 110328](images/Screenshot%202025-09-16%20110328.png)
![Screenshot 2025-09-16 110245](images/Screenshot%202025-09-16%20110245.png)
![Screenshot 2025-09-16 110207](images/Screenshot%202025-09-16%20110207.png)
![Screenshot 2025-09-16 110200](images/Screenshot%202025-09-16%20110200.png)
![Screenshot 2025-09-16 110131](images/Screenshot%202025-09-16%20110131.png)
![Screenshot 2025-09-16 110107](images/Screenshot%202025-09-16%20110107.png)
![Screenshot 2025-09-16 110032](images/Screenshot%202025-09-16%20110032.png)
![Screenshot 2025-09-16 110015](images/Screenshot%202025-09-16%20110015.png)
![Screenshot 2025-09-16 110002](images/Screenshot%202025-09-16%20110002.png)
![Screenshot 2025-09-16 105951](images/Screenshot%202025-09-16%20105951.png)
![Screenshot 2025-09-16 105940](images/Screenshot%202025-09-16%20105940.png)
![Screenshot 2025-09-16 105930](images/Screenshot%202025-09-16%20105930.png)
![Screenshot 2025-09-16 105910](images/Screenshot%202025-09-16%20105910.png)
![Screenshot 2025-09-16 105901](images/Screenshot%202025-09-16%20105901.png)
![Screenshot 2025-09-16 105852](images/Screenshot%202025-09-16%20105852.png)
![Screenshot 2025-09-16 105834](images/Screenshot%202025-09-16%20105834.png)
![Screenshot 2025-09-15 231113](images/Screenshot%202025-09-15%20231113.png)
![Screenshot 2025-09-15 230529](images/Screenshot%202025-09-15%20230529.png)
![Screenshot 2025-09-15 225932](images/Screenshot%202025-09-15%20225932.png)
![Screenshot 2025-09-15 225430](images/Screenshot%202025-09-15%20225430.png)
![Screenshot 2025-09-15 222729](images/Screenshot%202025-09-15%20222729.png)
![Screenshot 2025-09-15 222716](images/Screenshot%202025-09-15%20222716.png)
![Screenshot 2025-09-15 220509](images/Screenshot%202025-09-15%20220509.png)
![Screenshot 2025-09-15 220431](images/Screenshot%202025-09-15%20220431.png)
![Screenshot 2025-09-15 220415](images/Screenshot%202025-09-15%20220415.png)
![Screenshot 2025-09-15 215453](images/Screenshot%202025-09-15%20215453.png)
