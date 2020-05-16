import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer
{
    public static void main(String[] args) throws IOException
    {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("localhost", 8001), 0);
    }
}
