package com.msc.ms.users.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class HeaderService {
    private final Map<String, String> headers;

    public HeaderService(@Value("${msc.security.header}") String headerName, @Value("${msc.security.own.key}") String key) {
        this.headers = new HashMap<>();
        headers.put(headerName, key);
    }
}
