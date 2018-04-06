package com.example.winme.mylauncher;

/**
 * Created by winme on 3/23/18.
 */



import android.content.Context;
import android.os.Environment;
import android.os.PatternMatcher;
import android.webkit.MimeTypeMap;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
        String Filepath = context.getFilesDir().toString();
        if(Method.GET.equals(method)){
            String url = session.getUri();
            if(url.equals("/")){
                FileInputStream index =null;
                try {
                    index = new FileInputStream(Filepath + "/index.html");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return Response.newChunkedResponse(Status.OK, "text/html",index);
            }
            else {
                String regEx = ".html|.png";
                Pattern pattern = Pattern.compile(regEx);
                if (pattern.matcher(url).find()){
                    FileInputStream resource = null;
                    try {
                        resource = new FileInputStream(Filepath + url);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String mimetype = null;
                    String extension = MimeTypeMap.getFileExtensionFromUrl(Filepath + url);
                    if (extension != null) {
                        mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    }
                    return Response.newChunkedResponse(Status.OK, mimetype, resource);
                }
            }
        }
        else if (Method.POST.equals(method)){
            Map<String, String> body = new HashMap<String, String>();
            try {
                session.parseBody(body);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResponseException e) {
                e.printStackTrace();
            }
            String postBody = session.getQueryParameterString();
            String timezone = session.getParameters().get("timezone").toString();
            timezone = timezone.replaceFirst("\\[","");
            timezone = timezone.replaceFirst("]","");
            MyLauncher.timezone = timezone;
            return Response.newFixedLengthResponse( "<html><body>" + timezone + "</body></html>\n");
        }
        return Response.newFixedLengthResponse("not found");
    }
}
