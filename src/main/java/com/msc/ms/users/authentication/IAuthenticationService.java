package com.msc.ms.users.authentication;

import com.msc.ms.users.authentication.model.LogPassHistoryRequest;
import com.msc.ms.users.authentication.model.LogPasswordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${msc.services.authentication.url}", name = "msc-ms-authentication")
public interface IAuthenticationService {
    @GetMapping("/password")
    ResponseEntity<String> getPassword(@Header(name = "${msc.security.header}", value = "${msc.security.key}") @RequestParam("size") int pSize);

    @PostMapping("/password/log")
    ResponseEntity<LogPasswordResponse> getLogPassword(@Header(name = "${msc.security.header}", value = "${msc.security.key}") @RequestBody LogPassHistoryRequest pRequest);
}
