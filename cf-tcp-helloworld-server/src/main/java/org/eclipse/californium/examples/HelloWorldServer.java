/*******************************************************************************
 * Copyright (c) 2014 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 ******************************************************************************/
package org.eclipse.californium.examples;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoAPEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.tcp.server.TcpServerConnector;


public class HelloWorldServer extends CoapServer {
    
    private static final int BIND_PORT = 5684;
	private static final String BIND_ADDRESS = "0.0.0.0";

	/*
     * Application entry point.
     */
    public static void main(final String[] args) throws InterruptedException, ExecutionException, IOException {
        
        try {
            // create server
            final HelloWorldServer server = new HelloWorldServer();
            //uncomment to use SSL
//            final String keystore = "/keystore/path/complete/example/server.ks";
//        	  final SSLContextProvider contextProvider = new SSLContextProvider("TLS", "ThePassword", new String[]{keystore});
//            final TlsServerConnector connectorTls = new TlsServerConnector(BIND_ADDRESS, BIND_PORT, 
//            													  contextProvider.getSingletonSSLContext(), 
//            													  SSLClientCertReq.NONE, 
//            													  "TLSv1.2", "TLSv1.1");
            final TcpServerConnector connectorTcp = new TcpServerConnector(BIND_ADDRESS, BIND_PORT);
            final CoAPEndpoint enpoint = new CoAPEndpoint(connectorTcp, NetworkConfig.getStandard());
            server.addEndpoint(enpoint);
            final Map<InetSocketAddress, Future<?>> started = server.start();
            final Future<?> result = started.get(new InetSocketAddress(BIND_ADDRESS, BIND_PORT));
            result.get();
        } catch (final SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }
    
    /*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public HelloWorldServer() throws SocketException {
        
        // provide an instance of a Hello-World resource
        add(new HelloWorldResource());
    }
    
    /*
     * Definition of the Hello-World Resource
     */
    class HelloWorldResource extends CoapResource {
        
        public HelloWorldResource() {
            
            // set resource identifier
            super("helloWorld");
            
            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }
        
        @Override
        public void handleGET(final CoapExchange exchange) {
            
            // respond to the request
            exchange.respond("Hello World!");
        }
    }
}
