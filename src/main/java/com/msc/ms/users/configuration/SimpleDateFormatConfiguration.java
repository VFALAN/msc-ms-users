package com.msc.ms.users.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class SimpleDateFormatConfiguration {


    @Bean
    public SimpleDateFormat simpleDateFormat(@Value("${msc.app.date.format}") final String pDateFormatStr) {
        return new SimpleDateFormat(pDateFormatStr);

    }
}
