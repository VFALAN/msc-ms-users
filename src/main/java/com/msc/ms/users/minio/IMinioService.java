package com.msc.ms.users.minio;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${msc.services.minio.url}", name = "msc-ms-file")
public interface IMinioService {

    @PostMapping("/file/defaultUserImage")
    ResponseEntity<String> defaultImage(@RequestParam(name = "userId") Integer idUser);
}
