package io.xenn.android.http;

import android.graphics.Bitmap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class BitmapDownloadTaskTest {

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
    public void it_should_return_null_when_url_does_not_contain_bitmap() throws Exception {

        mockServer.when(request()
                .withMethod("GET")
                .withPath("/nonimage"))
                .respond(response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("aaa".getBytes())
                );

        BitmapDownloadTask bitmapDownloadTask = new BitmapDownloadTask("http://127.0.0.1:" + serverPort + "/nonimage");
        Bitmap bitmap = bitmapDownloadTask.getBitmap();
        assertNull(bitmap);
    }
}