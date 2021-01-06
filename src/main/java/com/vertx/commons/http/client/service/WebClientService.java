package com.vertx.commons.http.client.service;

import com.vertx.commons.http.client.core.HttpClient;
import com.vertx.commons.http.client.verticle.WebClientVerticle;

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
  
  public static Future<HttpClient> getClient(Vertx vertx, String name) {
    Promise<HttpClient> promise = Promise.promise();
    vertx.eventBus().<HttpClient>request(WebClientVerticle.ADDRESS, name).onSuccess(client -> {
      if (client.body() == null)
        promise.fail("No match Web Client: " + name + " reason -> client is null");
      else
        promise.complete(client.body());
    }).onFailure(cause -> promise
        .fail("No one verticle response for address: " + WebClientVerticle.ADDRESS + " reason -> " + cause));
    return promise.future();
  }
}