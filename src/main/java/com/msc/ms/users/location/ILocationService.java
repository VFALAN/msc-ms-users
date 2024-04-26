package com.msc.ms.users.location;

import feign.HeaderMap;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(url = "${msc.services.location.url}", name = "msc-ms-address")
public interface ILocationService {

    @GetMapping("/locality/{idLocation}")
    ResponseEntity<LocationResponse> getLocation(
            @PathVariable(name = "idLocation") Integer idLocation);
}
