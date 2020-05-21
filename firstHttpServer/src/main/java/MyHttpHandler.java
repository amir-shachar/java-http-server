import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MyHttpHandler implements HttpHandler
{
    private Map<String, UserSiteVisitDetails> entryMap = new HashMap();

    public void handle(HttpExchange httpExchange) throws IOException
    {
        String clientId = extractClientId(httpExchange);
        if (preventDOSAttack(clientId))
        {
            String requestParamValue = null;

            if ("GET".equals(httpExchange.getRequestMethod()))
            {
                requestParamValue = extractClientId(httpExchange);
            }
            handleResponse(httpExchange, requestParamValue);
        }
        else
        {
            sendServiceUnavailable(httpExchange);
        }

    }

    private boolean preventDOSAttack(String clientId)
    {
        boolean isValid = true;
        if (entryMap.containsKey(clientId))
        {
            UserSiteVisitDetails userVisit = entryMap.get(clientId);
            if (userVisit.hasTimeWindowExpired())
            {
                userVisit.startWindow();
            }
            else
            {
                if (userVisit.hasExceededVisits())
                {
                    isValid = false;
                }
                else
                {
                    userVisit.addVisit();
                }
            }
        }
        else
        {
            entryMap.put(clientId, new UserSiteVisitDetails());
        }
        return isValid;
    }

    private void sendServiceUnavailable(HttpExchange httpExchange) throws IOException
    {
        OutputStream outputStream = httpExchange.getResponseBody();
        String html = "<html>" +
                "<body>" +
                "<h1>" +
                "Service Denied! " +
                "</h1>" +
                "</body>" +
                "</html>";
        httpExchange.sendResponseHeaders(503, html.length());
        outputStream.write(html.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String extractClientId(HttpExchange httpExchange)
    {
        return httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException
    {
        OutputStream outputStream = httpExchange.getResponseBody();
        String htmlBuilder = "<html>" +
                "<body>" +
                "<h1>" +
                "Hello " +
                requestParamValue +
                "</h1>" +
                "</body>" +
                "</html>";
        String htmlResponse = htmlBuilder;
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();

    }
}