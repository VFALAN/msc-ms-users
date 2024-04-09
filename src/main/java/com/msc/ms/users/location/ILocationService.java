package com.msc.ms.users.location;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(url = "${msc.services.location.url}",name = "msc-ms-address")
public interface ILocationService {

    @GetMapping("/locality/{idLocation}")
    ResponseEntity<LocationResponse> getLocation(@Header(name = "${msc.security.header}", value = "${msc.security.key}") @PathVariable(name = "idLocation") Integer idLocation);
}
