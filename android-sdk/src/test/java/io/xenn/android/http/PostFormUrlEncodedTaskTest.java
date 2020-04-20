package io.xenn.android.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.robolectric.RobolectricTestRunner;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RunWith(RobolectricTestRunner.class)
public class PostFormUrlEncodedTaskTest {

    private ClientAndServer mockServer;
    private int serverPort;

    @Before
    public void startMockServer() {
        Random random = new Random();
        serverPort = random.nextInt(1000) + 7000;
        mockServer = startClientAndServer(serverPort);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void it_should_call_http_server_with_given_method_and_post_parameters() throws Exception {
        String payload = "e=base64formVariables";

        mockServer.when(request()
                .withMethod("POST")
                .withContentType(MediaType.APPLICATION_FORM_URLENCODED)
                .withPath("/")
                .withBody(payload.getBytes()))
                .respond(response().withStatusCode(201).withBody(payload.getBytes()));

        PostFormUrlEncodedTask httpService = new PostFormUrlEncodedTask("http://127.0.0.1:" + serverPort + "/", payload);
        int status = httpService.doInBackground();
        assertEquals(201, status);
    }


    @Test
    public void it_should_return_zero_as_status_code_when_api_call_fails() throws Exception {
        String payload = "e=base64formVariables";
        PostFormUrlEncodedTask httpService = new PostFormUrlEncodedTask("http://unknownhost:" + serverPort + "/", payload);
        int status = httpService.doInBackground();
        assertEquals(0, status);
    }

}