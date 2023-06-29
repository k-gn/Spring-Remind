package com.app.global.error.feign;

import org.springframework.http.HttpStatus;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/*
	- FeignClient 요청 시 200번대 http status 코드를 반환 받을 경우 정상으로 판단.
	- 그 외 경우를 처리할 ErrorDecoder 구현
		- 500 번대 http status 를 반환 받을 경우 재시도
 */
@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {

	private final ErrorDecoder errorDecoder = new Default();

	@Override
	public Exception decode(
		String methodKey,
		Response response
	) {
		log.error("{} 요청 실패, status : {}, reason : {}", methodKey, response.status(), response.reason());
		FeignException exception = FeignException.errorStatus(methodKey, response);
		HttpStatus httpStatus = HttpStatus.valueOf(response.status());
		if (httpStatus.is5xxServerError()) {
			return new RetryableException(
				response.status(),
				exception.getMessage(),
				response.request().httpMethod(),
				exception,
				null,
				response.request());
		}
		return errorDecoder.decode(methodKey, response); // return exception
	}
}
