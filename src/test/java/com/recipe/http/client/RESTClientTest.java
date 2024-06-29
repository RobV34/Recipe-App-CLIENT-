package com.recipe.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.http.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RESTClientTest {

    private RESTClient restClient;

    @Mock
    private HttpClient mockHttpClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        restClient = new RESTClient();
        restClient.getClient();
        restClient.setServerURL("http://localhost:8080");
    }

    @Test
    public void testGetRecipeByName() throws IOException, InterruptedException {

        String responseBody = "{\"name\":\"Test Recipe\",\"ingredients\":[{\"name\":\"Ingredient 1\"}],\"instructions\":\"Test Instructions\"}";


        mockHttpResponse(responseBody, 200);


        Recipe recipe = restClient.getRecipeByName(responseBody);


        assertEquals("Test Recipe", recipe.getName());
        assertEquals("Test Instructions", recipe.getInstructions());
    }


    @Test
    void testServerReturnsError() {
        RESTClient client = new RESTClient();

        RESTClient mockedClient = Mockito.mock(RESTClient.class);
        try {
            when(mockedClient.getRecipeByName(anyString())).thenThrow(new RuntimeException("Server error"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> {
            mockedClient.getRecipeByName("testEndpoint");
        });
    }
    private void mockHttpResponse(String responseBody, int statusCode) throws IOException, InterruptedException {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(statusCode);
        when(mockResponse.body()).thenReturn(responseBody);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
    }
}
