package com.music.lover.hometask.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.lover.hometask.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class ItunesRestTemplate extends RestTemplate {

    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(ItunesRestTemplate.class);

    public ItunesRestTemplate(
            @Value("${apple.itunes.url}") String baseUrl,
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
        this.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
    }

    public <T> T executePostRequest(String uri, Class<T> objectClass) throws ServiceException {
        T response;

        logger.info("[B] Itunes call");

        try {
            String stringResponse = this.postForObject(
                    uri,
                    null,
                    String.class
            );

            response = objectMapper.readValue(stringResponse, objectClass);
        } catch (RestClientException | JsonProcessingException e) {
            throw new ServiceException("Get artists request failed");
        }

        if (response == null) {
            throw new ServiceException("Response is null");
        }

        logger.info("[E] Itunes call");

        return response;
    }

}
