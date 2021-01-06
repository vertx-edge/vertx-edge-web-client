package com.vertx.commons.http.client.codec;

import com.vertx.commons.annotations.EventBusCodec;
import com.vertx.commons.http.client.core.HttpClient;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Luiz Schmidt
 */
@EventBusCodec
public class RestClientCodec implements MessageCodec<HttpClient, HttpClient> {

	@Override
	public void encodeToWire(Buffer buffer, HttpClient s) {
		//Nothing to do
	}

	@Override
	public HttpClient decodeFromWire(int pos, Buffer buffer) {
		return null;
	}

	@Override
	public HttpClient transform(HttpClient s) {
		return s;
	}

	@Override
	public String name() {
		return HttpClient.codec();
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}
