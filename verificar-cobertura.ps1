$ErrorActionPreference = "Stop"

Write-Host "Ejecutando pruebas de UsuarioService..."
Push-Location ".\UsuarioService"
mvn clean verify
Start-Process ".\target\site\jacoco\index.html"
Pop-Location

Write-Host "Ejecutando pruebas de GotyStore_JPA..."
Push-Location ".\GotyStore_JPA"
mvn clean verify
Start-Process ".\target\site\jacoco\index.html"
Pop-Location

Write-Host "Pruebas y validación de cobertura finalizadas."
