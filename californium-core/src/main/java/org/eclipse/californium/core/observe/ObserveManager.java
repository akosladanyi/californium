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
 *    Martin Lanter - architect and re-implementation
 *    Dominique Im Obersteg - parsers and initial implementation
 *    Daniel Pauli - parsers and initial implementation
 *    Kai Hudalla - logging
 ******************************************************************************/
package org.eclipse.californium.core.observe;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The observe manager holds a mapping of endpoint addresses to
 * {@link ObservingEndpoint}s. It makes sure that there be only one
 * ObservingEndpoint that represents the observe relations from one endpoint to
 * this server. This important in case we want to cancel all relations to a
 * specific endpoint, e.g., when a confirmable notification timeouts.
 * <p>
 * Notice that each server has its own ObserveManager. If a server binds to
 * multiple endpoints, the ObserveManager keeps the observe relations for all of
 * them.
 */
//TODO: find a better name... how about ObserveObserver -.-
public interface ObserveManager {
	
	/**
	 * Find the ObservingEndpoint for the specified endpoint address or create
	 * a new one if none exists yet. Does not return null.
	 * 
	 * @param address the address
	 * @return the ObservingEndpoint for the address
	 */
	public ObservingEndpoint findObservingEndpoint(InetSocketAddress address);
	
	/**
	 * Return the ObservingEndpoint for the specified endpoint address or null
	 * if none exists.
	 * 
	 * @param address the address
	 * @return the ObservingEndpoint or null
	 */
	public ObservingEndpoint getObservingEndpoint(InetSocketAddress address);
	
	public ObserveRelation getRelation(InetSocketAddress source, byte[] token);
	
}
