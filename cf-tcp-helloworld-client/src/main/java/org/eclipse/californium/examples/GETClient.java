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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.network.CoAPEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.elements.tcp.client.TcpClientConnector;


public class GETClient {

	/*
	 * Application entry point.
	 * 
	 */	
	public static void main(final String args[]) throws NoSuchAlgorithmException, InterruptedException, ExecutionException, IOException, KeyManagementException {
		
		
		if (args.length >= 4) {
			
			final String coapScheme = args[0];
			final String remoteAddress = args[1];
			final int remotePort = Integer.parseInt(args[2]);
			final String[] resources = Arrays.copyOfRange(args, 3, args.length);
			
			final CoapClient client = new CoapClient(coapScheme, remoteAddress, remotePort, resources);	
			//uncomment to use SSL
//			final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//			sslContext.init(null, null, null);
//			final TlsClientConnector connectorTls = new TlsClientConnector(remoteAddress, remotePort, sslContext);
			final TcpClientConnector connectorTcp = new TcpClientConnector(remoteAddress, remotePort);
			final CoAPEndpoint endpoint  = new CoAPEndpoint(connectorTcp, NetworkConfig.getStandard());
			final Future<?> result = endpoint.start();//endpoint MUST be stared first
			result.get();
			
			client.setEndpoint(endpoint);
			client.get(new CoapHandler() {
				
				@Override
				public void onLoad(final CoapResponse response) {
					System.out.println(response.getCode());
					System.out.println(response.getOptions());
					System.out.println(response.getResponseText());
					
					System.out.println("\nADVANCED\n");
					// access advanced API with access to more details through .advanced()
					System.out.println(Utils.prettyPrint(response));
				}
				
				@Override
				public void onError() {
					System.out.println("No response received. ERROR");
				}
			});			
		} else {
			// display help
			System.out.println("Californium (Cf) GET Client");
			System.out.println("(c) 2014, Institute for Pervasive Computing, ETH Zurich");
			System.out.println();
			System.out.println("Usage: " + GETClient.class.getSimpleName() + " URI");
			System.out.println("  URI: The CoAP URI of the remote resource to GET");
		}
	}

}
