package servnow.servnow.api.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import servnow.servnow.api.dto.ServnowResponse;

@RestControllerAdvice(basePackages = "servnow.servnow.api")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    return returnType.getParameterType() == ServnowResponse.class;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    if (returnType.getParameterType() == ServnowResponse.class) {
      HttpStatus status = HttpStatus.valueOf(((ServnowResponse<?>) body).getCode());
      response.setStatusCode(status);
    }
    return body;
  }
}
