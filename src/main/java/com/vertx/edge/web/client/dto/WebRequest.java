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
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class WebRequest {

  private final BodyType bodyType;
  @Builder.ObtainVia(method = "bodyWithJson")
  private Buffer body;
  private MultiMap formBody;
  private MultipartForm multipartFormBody;
  
  private long timeout;
  private String encoding;
  
  @Builder.Default
  private JsonObject headers = new JsonObject();
  @Builder.Default
  private JsonObject queryParams = new JsonObject();
  @Builder.Default
  private JsonObject pathParams = new JsonObject();
  
  public static class WebRequestBuilder {
    
    public WebRequestBuilder body(String body) {
      this.body = new BufferImpl().appendString(body);
      this.bodyType = BodyType.BUFFER;
      return this;
    }
    
    public WebRequestBuilder body(JsonObject body) {
      this.body = body.getBuffer("UTF-8");
      return this;
    }
    
    public WebRequestBuilder body(Buffer body) {
      this.body = body;
      this.bodyType = BodyType.BUFFER;
      return this;
    }
  }
}
