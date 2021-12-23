package chewyt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class HttpClientConnection implements Runnable {

    private Socket socket;
    private String[] docRoot;

    public HttpClientConnection(Socket socket, String[] docRoot) {
        this.socket = socket;
        this.docRoot = docRoot;
    }

    @Override
    public void run() {

        try (InputStream is = socket.getInputStream()) {

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String request = br.readLine();
            System.out.println(request);
            // parsing of the request commandline
            String command = request.split(" ")[0];
            String resource = request.split(" ")[1];

            if (resource.equals("/")) {
                resource = "/index.html";
            }

            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            HttpWriter writer = new HttpWriter(bos);
            // Action 1
            if (!command.equals("GET")) {
                System.out.println("405 error");
                writer.writeString("HTTP/1.1 405 Method Not Allowed");
                writer.writeString();
                writer.writeString("%s not supported".formatted(command));

            } else {
                // Action 2
                Optional<File> opt = getFile(resource, docRoot);
                if (opt.isPresent()) {
                    // resource found
                    File f = opt.get();
                    FileInputStream fis = new FileInputStream(f);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    byte[] buffer = new byte[4096];
                    int size = 0;

                    writer.writeString("HTTP/1.1 200 OK");
                    // check if it is an png image
                    if (resource.endsWith(".png")) {
                        writer.writeString("Content-Type: image/png");
                        System.out.println("Image resource");
                    } else {
                        System.out.println("HTML resource");
                    }
                    writer.writeString();

                    while (-1 != (size = bis.read(buffer, 0, buffer.length))) {
                        writer.writeBytes(buffer, 0, size);
                    }
                    bis.close();

                } else {
                    // no resource found, not null
                    System.out.println("404 error");
                    writer.writeString("HTTP/1.1 404 Not Found");
                    writer.writeString();
                    writer.writeString("%s not found".formatted(resource));

                }

            }
            writer.close();
            is.close();
            os.close();

        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException e2) {
            }
        }
    }

    public Optional<File> getFile(String resource, String[] docRoot) {

        for (String path : docRoot) {
            File resourcePath = new File(path + "/" + resource);
            if (resourcePath.exists() && resourcePath.isFile()) {
                return Optional.of(resourcePath);
            }
        }
        return Optional.empty();
    }

}
