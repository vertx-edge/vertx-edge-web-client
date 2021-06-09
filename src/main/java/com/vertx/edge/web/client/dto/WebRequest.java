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
package com.vertx.edge.web.client.dto;

import com.vertx.edge.web.client.enumeration.BodyType;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.multipart.MultipartForm;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WebRequest {

  private BodyType bodyType = BodyType.NO_BODY;
  @Builder.ObtainVia(method = "bodyWithJson")
  private Buffer body;
  private MultiMap formBody;
  private MultipartForm multipartFormBody;

  private Long timeout;
  private String encoding;

  private JsonObject headers = new JsonObject();
  private JsonObject queryParams = new JsonObject();
  private JsonObject pathParams = new JsonObject();

  public WebRequest addPathParam(String key, Object value) {
    this.pathParams.put(key, value);
    return this;
  }
  
  public WebRequest addQueryParam(String key, Object value) {
    this.queryParams.put(key, value);
    return this;
  }
  
  public WebRequest addHeader(String key, Object value) {
    this.headers.put(key, value);
    return this;
  }
  
  public WebRequest setBody(String body) {
    this.body = new BufferImpl().appendString(body);
    this.bodyType = BodyType.BUFFER;
    return this;
  }

  public WebRequest setBody(JsonObject body) {
    this.body = body.toBuffer();
    this.bodyType = BodyType.JSON;
    return this;
  }

  public WebRequest setBody(Buffer body) {
    this.body = body;
    this.bodyType = BodyType.BUFFER;
    return this;
  }
}
