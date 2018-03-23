package com.example.winme.mylauncher;

/**
 * Created by winme on 3/23/18.
 */


import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;
    public MyServer() throws IOException {
        super(PORT);
        start();
        System.out.println("\nRunning! Point your browers to http://localhost:8080/ \\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + session.getUri() + " !</p>";
        return Response.newFixedLengthResponse( msg + "</body></html>\n" );
    }
}
