import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class MainServer
{
    public static void main(String[] args) throws IOException
    {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("localhost", 8001), 0);

        server.createContext("/amir", new  MyHttpHandler());
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        server.setExecutor(executor);

        server.start();

        Logger.getAnonymousLogger().info(" Server started on port 8001");
    }


}
