package com.msc.ms.users.security.filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeadresrequestInterceptor implements RequestInterceptor {

    @Value("${msc.security.header}")
    private String headerName;
    @Value("${msc.security.own.key}")
    private String key;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(this.headerName, this.key);
    }
}
