package com.vertx.commons.http.client.codec;

import com.vertx.commons.annotations.EventBusCodec;
import com.vertx.commons.http.client.core.WebClient;

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
