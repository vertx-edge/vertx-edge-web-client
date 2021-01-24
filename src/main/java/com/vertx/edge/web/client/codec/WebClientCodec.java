/*
 * Vert.x Edge, open source.
 * Copyright (C) 2020-2021 Vert.x Edge
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.vertx.edge.web.client.codec;

import com.vertx.edge.annotations.EventBusCodec;
import com.vertx.edge.web.client.core.WebClient;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Luiz Schmidt
 */
@EventBusCodec
public class WebClientCodec implements MessageCodec<WebClient, WebClient> {

	@Override
	public void encodeToWire(Buffer buffer, WebClient s) {
		//Nothing to do
	}

	@Override
	public WebClient decodeFromWire(int pos, Buffer buffer) {
		return null;
	}

	@Override
	public WebClient transform(WebClient s) {
		return s;
	}

	@Override
	public String name() {
		return WebClient.codec();
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
