package com.vertx.commons.web.client.service;

import com.vertx.commons.web.client.core.WebClient;
import com.vertx.commons.web.client.verticle.WebClientVerticle;

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