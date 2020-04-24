package io.xenn.android.http;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RunWith(AndroidJUnit4.class)
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
    public void it_should_return_downloaded_bitmap() throws Exception {
        String imagePath = this.getClass().getClassLoader().getResource("bird.jpg").getFile();

        mockServer.when(request()
                .withMethod("GET")
                .withPath("/bird.jpg"))
                .respond(response()
                        .withStatusCode(200)
                        .withContentType(MediaType.JPEG)
                        .withBody(Files.readAllBytes(Paths.get(imagePath)))
                );

        BitmapDownloadTask bitmapDownloadTask = new BitmapDownloadTask("http://127.0.0.1:" + serverPort + "/bird.jpg");
        Bitmap bitmap = bitmapDownloadTask.getBitmap();
        assertEquals(256, bitmap.getWidth());
        assertEquals(256, bitmap.getHeight());
    }

    @Test
    public void it_should_return_null_when_url_returns_404() {
        mockServer.when(request()
                .withMethod("GET")
                .withPath("/imagenotfound"))
                .respond(response().withStatusCode(404));

        BitmapDownloadTask bitmapDownloadTask = new BitmapDownloadTask("http://127.0.0.1:" + serverPort + "/imagenotfound");
        Bitmap bitmap = bitmapDownloadTask.getBitmap();
        assertNull(bitmap);
    }
}