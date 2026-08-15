# Create logs and data directory if not exists
New-Item -ItemType Directory -Force -Path .\logs | Out-Null
New-Item -ItemType Directory -Force -Path .\data | Out-Null

$javaExe = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME 'bin\java.exe' } else { 'java' }
Stop-Process -Name java -Force -ErrorAction SilentlyContinue

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Building all microservices..." -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$services = @("eureka-server", "config-server", "api-gateway", "user-service", "vehicle-service", "parking-service", "payment-service")

foreach ($service in $services) {
    Write-Host "Building $service..." -ForegroundColor Yellow
    Push-Location $service
    & .\mvnw.cmd clean package -DskipTests
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Failed to build $service" -ForegroundColor Red
        Pop-Location
        exit 1
    }
    Pop-Location
}

Write-Host "=========================================" -ForegroundColor Green
Write-Host "All microservices built successfully!" -ForegroundColor Green
Write-Host "Starting microservices in order..." -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green

function Start-App {
    param(
        [string]$JarPath,
        [string]$Port,
        [string]$LogPrefix
    )

    $quotedJar = '"' + $JarPath + '"'
    Start-Process -FilePath $javaExe -ArgumentList @('-jar', $quotedJar, "--server.port=$Port") -RedirectStandardOutput "logs/${LogPrefix}.log" -RedirectStandardError "logs/${LogPrefix}-error.log" -NoNewWindow
}

# 1. Start Eureka Server (Port 8761)
Write-Host "Starting Eureka Server..." -ForegroundColor Yellow
$eurekaJar = (Resolve-Path "eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar").Path
Start-App -JarPath $eurekaJar -Port '8761' -LogPrefix 'eureka-server'
Start-Sleep -Seconds 12

# 2. Start Config Server (Port 8888)
Write-Host "Starting Config Server..." -ForegroundColor Yellow
$configJar = (Resolve-Path "config-server/target/config-server-0.0.1-SNAPSHOT.jar").Path
Start-App -JarPath $configJar -Port '8888' -LogPrefix 'config-server'
Start-Sleep -Seconds 12

# 3. Start Business & Gateway Services
Write-Host "Starting API Gateway and Business Microservices..." -ForegroundColor Yellow

$gatewayJar = (Resolve-Path "api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar").Path
$uJar = (Resolve-Path "user-service/target/user-service-0.0.1-SNAPSHOT.jar").Path
$vJar = (Resolve-Path "vehicle-service/target/vehicle-service-0.0.1-SNAPSHOT.jar").Path
$pJar = (Resolve-Path "parking-service/target/parking-service-0.0.1-SNAPSHOT.jar").Path
$payJar = (Resolve-Path "payment-service/target/payment-service-0.0.1-SNAPSHOT.jar").Path

Start-App -JarPath $gatewayJar -Port '8080' -LogPrefix 'api-gateway'
Start-App -JarPath $uJar -Port '8081' -LogPrefix 'user-service'
Start-App -JarPath $vJar -Port '8082' -LogPrefix 'vehicle-service'
Start-App -JarPath $pJar -Port '8083' -LogPrefix 'parking-service'
Start-App -JarPath $payJar -Port '8084' -LogPrefix 'payment-service'

Write-Host "=========================================" -ForegroundColor Green
Write-Host "All services started! Check the logs/ folder for output details." -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
