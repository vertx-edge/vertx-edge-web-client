package com.vertx.commons.web.client.dto;

import com.vertx.commons.web.client.enumeration.BodyType;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.multipart.MultipartForm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
    private Buffer body;
    private BodyType bodyType;
    
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
