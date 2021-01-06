package com.vertx.commons.http.client.dto;

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
