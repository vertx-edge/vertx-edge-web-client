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
package com.vertx.edge.web.client.core;

import java.util.Map.Entry;
import java.util.Objects;

import com.vertx.edge.utils.Timer;
import com.vertx.edge.web.client.dto.WebRequest;
import com.vertx.edge.web.client.dto.WebResponse;
import com.vertx.edge.web.client.dto.WebResponse.WebResponseBuilder;

import io.netty.handler.codec.http.HttpClientCodec;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;

/**
 * @author Luiz Schmidt
 */
public class WebClient {

  private static final String DEFAULT_ENCODING = "UTF-8";
  private static final String DEFAULT_CONTENT_TYPE = "application/json";
  private static final int DEFAULT_HTTP_PORT = 443;
  private static final int DEFAULT_TIMEOUT = 30_000;

  
  private io.vertx.ext.web.client.WebClient client;
  private JsonObject config;

  private String host;
  private int port;
  private String resource;
  private HttpMethod method;

  private JsonArray pathParams;

  public WebClient(io.vertx.ext.web.client.WebClient client, JsonObject configJson) {
    this.config = configJson.copy();
    this.client = client;

    this.host = config.getString("hostname");
    this.resource = config.getString("resource");
    this.method = HttpMethod.valueOf(config.getString("method", "GET"));
    this.port = config.getInteger("port", DEFAULT_HTTP_PORT);

    Objects.requireNonNull(host, "hostname is missing.");
    Objects.requireNonNull(resource, "resource is missing.");

    config.put("contentType", config.getString("contentType", DEFAULT_CONTENT_TYPE));
    config.put("encoding", config.getString("encoding", DEFAULT_ENCODING));
  }

  public void send(WebRequest request, Handler<AsyncResult<WebResponse>> handler) {
    this.send(request).onComplete(handler);
  }

  public Future<WebResponse> send(WebRequest request) {
    HttpRequest<Buffer> clientRequest = this.client.request(method, this.port, this.host, this.resource);
    if(request.getTimeout() != null) {
      clientRequest.timeout(request.getTimeout());
    }else {
      clientRequest.timeout(config.getInteger("timeout", DEFAULT_TIMEOUT));
    }

    if (pathParams != null) {
      for (int i = 0; i < pathParams.size(); i++) {
        String param = pathParams.getString(i);
        Object value = request.getPathParams().getValue(param);
        Objects.requireNonNull(value, "The PathParam is required: " + param);
        resource = this.resource.replaceAll("(\\{" + param + "\\})", value.toString());
      }
    }

    for (Entry<String, Object> param : request.getQueryParams())
      clientRequest.addQueryParam(param.getKey(), param.getValue() != null ? param.getValue().toString() : "");

    for (Entry<String, Object> header : request.getHeaders())
      clientRequest.putHeader(header.getKey(), header.getValue() != null ? header.getValue().toString() : "");

    WebResponseBuilder response = WebResponse.builder();
    Timer timer = Timer.start();
    Promise<WebResponse> promise = Promise.promise();

    send(clientRequest, request).onComplete(res -> {
      timer.end();
      response.elapsedTime(timer.getTimeMillis()).request(request).response(res.result());
      if (res.succeeded()) {
        response.httpCode(res.result().statusCode()).message(buildSuccessMessage(res.result(), timer));
        promise.complete(response.build());
      } else {
        promise.tryFail(buildErrorMessage(res.cause(), timer));
      }
    });
    return promise.future();
  }

  private static Future<HttpResponse<Buffer>> send(HttpRequest<Buffer> clientRequest, WebRequest request) {
    switch (request.getBodyType()) {
      case NO_BODY:
        return clientRequest.send();
      case JSON:
        return clientRequest.sendJson(request.getBody());
      case BUFFER:
        return clientRequest.sendBuffer(request.getBody());
      case FORM:
        return clientRequest.sendForm(request.getFormBody());
      case MULTIPART_FORM:
        return clientRequest.sendMultipartForm(request.getMultipartFormBody());
      default:
        return Future.failedFuture(new IllegalArgumentException("Unexpected value: " + request.getBodyType()));
    }
  }

  private String buildSuccessMessage(HttpResponse<Buffer> response, Timer timer) {
    StringBuilder sb = new StringBuilder();
    sb.append(this.method).append(' ');
    sb.append(this.host).append(':').append(port).append(resource);
    sb.append(" | response ").append("[ HTTP ").append(response.statusCode()).append(" ]");

    if (response.body() != null) {
      sb.append(' ').append(response.bodyAsString(DEFAULT_ENCODING));
    }
    sb.append(" | time elapsed: ").append(timer);
    return sb.toString();
  }

  private String buildErrorMessage(Throwable cause, Timer timer) {
    StringBuilder sb = new StringBuilder();
    sb.append(this.method).append(' ');
    sb.append(this.host).append(':').append(port).append(resource);
    sb.append(" | failed ").append(cause.getMessage());
    sb.append(" | time elapsed: ").append(timer);
    return sb.toString();
  }
  
  public static String codec() {
    return HttpClientCodec.class.getName();
  }
}
