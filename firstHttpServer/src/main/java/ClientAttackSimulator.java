import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientAttackSimulator
{
    private boolean contactUrl = true;
    private int simulations;
    private ExecutorService executor;

    public ClientAttackSimulator(int simulations)
    {
        this.simulations = simulations;
        startServerCommunication();
    }

    private void startServerCommunication()
    {
        executor = Executors.newFixedThreadPool(simulations);
        for (int i = 1; i <= simulations; i++)
        {
            executor.submit(getSimulatedClientRunnable(String.valueOf(i)));
        }
    }

    private Runnable getSimulatedClientRunnable(String s)
    {
        return () -> {
            while (contactUrl)
            {
                try
                {
                    sendRequest(s);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                try
                {
                    Thread.sleep((int) (1000 * Math.random()));
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    private void sendRequest(String userId) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet("http://localhost:8001/ermetic/?clientId=" + userId));
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode + " id: " + userId);
    }

    public void shutdown() throws InterruptedException
    {
        contactUrl = false;
        executor.shutdown();
        executor.awaitTermination(2l, TimeUnit.MINUTES);
    }
}
