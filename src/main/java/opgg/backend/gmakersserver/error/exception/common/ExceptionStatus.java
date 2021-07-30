package opgg.backend.gmakersserver.error.exception.common;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus{

	// Account
	ACCOUNT_NOT_FOUND_EXCEPTION(4001, "유저가 존재하지 않습니다.", NOT_FOUND),
	ACCOUNT_SAVE_FAILED_EXCEPTION(4002, "회원가입을 실패했습니다.", BAD_REQUEST),
	ACCOUNT_DUPLICATE_ID_EXCEPTION(4003, "중복된 아이디 입니다.", BAD_REQUEST),


	// Common
	RUN_TIME_EXCEPTION(500, "런타임 에러", INTERNAL_SERVER_ERROR),
	NOT_FOUND_EXCEPTION(404, "요청한 리소스가 존재하지 않습니다.", NOT_FOUND),
	INVALID_TYPE_VALUE_EXCEPTION(400, "유효하지 않는 Type의 값입니다. 입력 값을 확인 해주세요.", BAD_REQUEST),
	INVALID_FORMAT_EXCEPTION(400, "유효하지 않는 Type 입니다. Type을 확인 해주세요.", BAD_REQUEST),
	INVALID_INPUT_VALUE_EXCEPTION(400, "유효하지 않는 입력 값입니다.", BAD_REQUEST),
	METHOD_NOT_SUPPORT_EXCEPTION(405, "지원하지 않은 HTTP Method 입니다.", METHOD_NOT_ALLOWED),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;


}
