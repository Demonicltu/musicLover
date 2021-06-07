package com.music.lover.hometask.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.lover.hometask.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class ItunesRestTemplate extends RestTemplate {

    private final ObjectMapper objectMapper;

    public ItunesRestTemplate(
            @Value("${apple.itunes.url}") String baseUrl,
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
        this.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
    }

    public <T> T executePostRequest(String uri, Class<T> objectClass) throws ServiceException {
        try {
            String response = this.getForObject(
                    uri,
                    String.class
            );

            return objectMapper.readValue(response, objectClass);
        } catch (RestClientException | JsonProcessingException e) {
            throw new ServiceException("Get artists request failed");
        }
    }

}
