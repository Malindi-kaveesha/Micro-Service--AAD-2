package com.spms.parking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {
    @GetMapping("/api/vehicles/exists/{id}")
    Boolean exists(@PathVariable("id") Long id);
}
