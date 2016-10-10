package org.eclipse.jetty.demo;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class EventServer
{
    public static Server createServer(
    		final int port, final String contextPath, final boolean isCompanyAgent)
    {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextPath);
        context.setStopTimeout(10000000);
        server.setHandler(context);

        try
        {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(
            		isCompanyAgent ?
            			CompanyAgentEventSocket.class :
            			CustomerEventSocket.class);

            System.out.println("Server Started...");
            // server.start();
            // server.dump(System.err); 
            // server.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
            return null;
        }
        return server;
    }
}
