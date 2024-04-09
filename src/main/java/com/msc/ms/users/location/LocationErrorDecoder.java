package com.msc.ms.users.location;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;

@RequiredArgsConstructor
public class LocationErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 404 -> new BadRequestException("Resource not found");
            case 403 -> new BadCredentialsException("Access denied");
            case 401 -> new BadRequestException("Bad Request");
            case 500 -> new Exception("internal server error");
            default -> errorDecoder.decode(s, response);
        };
    }
}
