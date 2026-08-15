import urllib.request
import zipfile
import io
import os

services = [
    {
        "name": "eureka-server",
        "deps": "cloud-eureka-server",
        "pkg": "com.spms.eureka"
    },
    {
        "name": "config-server",
        "deps": "cloud-config-server,cloud-eureka",
        "pkg": "com.spms.config"
    },
    {
        "name": "api-gateway",
        "deps": "cloud-gateway,cloud-eureka",
        "pkg": "com.spms.gateway"
    },
    {
        "name": "user-service",
        "deps": "web,data-jpa,h2,cloud-eureka,validation",
        "pkg": "com.spms.user"
    },
    {
        "name": "vehicle-service",
        "deps": "web,data-jpa,h2,cloud-eureka,validation",
        "pkg": "com.spms.vehicle"
    },
    {
        "name": "parking-service",
        "deps": "web,data-jpa,h2,cloud-eureka,validation,cloud-feign",
        "pkg": "com.spms.parking"
    },
    {
        "name": "payment-service",
        "deps": "web,data-jpa,h2,cloud-eureka,validation",
        "pkg": "com.spms.payment"
    }
]

boot_version = "3.3.2"
java_version = "21"

for s in services:
    name = s["name"]
    deps = s["deps"]
    pkg = s["pkg"]
    print(f"Downloading {name}...")
    url = (
        f"https://start.spring.io/starter.zip?"
        f"type=maven-project&"
        f"language=java&"
        f"baseDir={name}&"
        f"groupId=com.spms&"
        f"artifactId={name}&"
        f"name={name}&"
        f"packageName={pkg}&"
        f"javaVersion={java_version}&"
        f"dependencies={deps}"
    )
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        with urllib.request.urlopen(req) as response:
            zip_content = response.read()
            with zipfile.ZipFile(io.BytesIO(zip_content)) as z:
                z.extractall(".")
        print(f"Extracted {name} successfully.")
    except Exception as e:
        print(f"Error downloading {name}: {e}")
