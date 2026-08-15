Write-Host "Stopping all Spring Boot microservices..." -ForegroundColor Yellow
Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like "*java*" -and ($_.CommandLine -like "*eureka-server*" -or $_.CommandLine -like "*config-server*" -or $_.CommandLine -like "*api-gateway*" -or $_.CommandLine -like "*user-service*" -or $_.CommandLine -like "*vehicle-service*" -or $_.CommandLine -like "*parking-service*" -or $_.CommandLine -like "*payment-service*") } | ForEach-Object {
    Write-Host "Stopping process ID $($_.ProcessId) ($($_.Name))" -ForegroundColor Cyan
    Stop-Process $_.ProcessId -Force
}
Write-Host "All microservices stopped." -ForegroundColor Green
