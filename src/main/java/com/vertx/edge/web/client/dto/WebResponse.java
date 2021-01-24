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

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebResponse {

  private static final int INIT_STATUS_CODE = 200;
  private static final int END_STATUS_CODE = 299;
  
  private Long elapsedTime;
  private int httpCode;
  private String message;
  private HttpResponse<Buffer> response;
  private WebRequest request;
  
  public boolean status() {
    return httpCode >= INIT_STATUS_CODE && httpCode <= END_STATUS_CODE;
  }
  
}
