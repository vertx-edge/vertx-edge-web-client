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
package com.vertx.edge.web.client.service;

import com.vertx.edge.web.client.core.WebClient;
import com.vertx.edge.web.client.verticle.WebClientVerticle;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * @author Luiz Schmidt
 */
public final class WebClientService {

  private WebClientService() {
    //nothing to do
  }
  
  public static Future<WebClient> getClient(Vertx vertx, String name) {
    Promise<WebClient> promise = Promise.promise();
    vertx.eventBus().<WebClient>request(WebClientVerticle.ADDRESS, name).onSuccess(client -> {
      if (client.body() == null)
        promise.fail("No match Web Client: " + name + " reason -> client is null");
      else
        promise.complete(client.body());
    }).onFailure(cause -> promise
        .fail("No one verticle response for address: " + WebClientVerticle.ADDRESS + " reason -> " + cause));
    return promise.future();
  }
}