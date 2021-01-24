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
package com.vertx.edge.web.client.verticle;

import java.util.HashMap;
import java.util.Map;

import com.vertx.edge.verticle.BaseVerticle;
import com.vertx.edge.web.client.core.WebClient;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.extern.log4j.Log4j2;

/**
 * @author Luiz Schmidt
 */
@Log4j2
public class WebClientVerticle extends BaseVerticle {

  public static final String ADDRESS = "web-client.get";

  private Map<String, WebClient> clients = new HashMap<>();

  @Override
  protected void up() {
    Thread.currentThread().setName("web-client");
    this.instanceHttpClientFromConfig();
    vertx.eventBus().<String>consumer(ADDRESS, this::getHttpClient);
  }

  private void getHttpClient(Message<String> message) {
    DeliveryOptions options = new DeliveryOptions().setCodecName(WebClient.codec());
    String clientName = message.body();
    if (this.clients.containsKey(clientName)) {
      message.reply(this.clients.get(clientName), options);
    } else {
      message.fail(0, "Not found client web as '" + clientName + "'");
    }
  }

  private void instanceHttpClientFromConfig() {
    JsonObject httpClientList = config().getJsonObject("clients");
    for (String name : httpClientList.fieldNames()) {
      JsonObject config = httpClientList.getJsonObject(name);
      JsonObject options = httpClientList.getJsonObject("options");

      if (options == null) {
        this.clients.put(name, new WebClient(io.vertx.ext.web.client.WebClient.create(vertx), config));
      } else {
        this.clients.put(name,
            new WebClient(io.vertx.ext.web.client.WebClient.create(vertx, new WebClientOptions(options)), config));
      }

      log.info("[web client] " + name + " created.");
    }
  }
}
