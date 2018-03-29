package com.example.winme.mylauncher;

/**
 * Created by winme on 3/23/18.
 */



import android.content.Context;
import android.os.Environment;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;
    private Context context;
    public MyServer(Context Lcontex) throws IOException {
        super(PORT);
        context = Lcontex;
        start();
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        if(Method.GET.equals(method)){
            String url = session.getUri();
            if(url.equals("/")){
                FileInputStream index =null;
                try {
                    index = new FileInputStream(context.getFilesDir() + "/index.html");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return Response.newChunkedResponse(Status.OK, "text/html",index);
            }
        }
        else if (Method.POST.equals(method)){
            String msg = "<html><body><h1>get post</h1>\n";
            return Response.newFixedLengthResponse(msg + "</body></html>\n");
        }
        return Response.newFixedLengthResponse("not found");
    }
}
