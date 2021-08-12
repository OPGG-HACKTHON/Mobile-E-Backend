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
	ACCOUNT_PASSWORD_NOT_MATCH_EXCEPTION(4004, "비밀번호 불일치", BAD_REQUEST),

	// Profile
	PROFILE_EXIST_EXCEPTION(4050, "이미 존재한 프로필 입니다.", BAD_REQUEST),

	// RiotAPI
	SUMMONER_NOT_FOUNT_EXCEPTION(4100, "존재하지 않는 소환사 입니다.", BAD_REQUEST),

	// Common
	RUN_TIME_EXCEPTION(500, "런타임 에러", INTERNAL_SERVER_ERROR),
	NOT_FOUND_EXCEPTION(404, "요청한 리소스가 존재하지 않습니다.", NOT_FOUND),
	INVALID_TYPE_VALUE_EXCEPTION(400, "유효하지 않는 Type의 값입니다. 입력 값을 확인 해주세요.", BAD_REQUEST),
	INVALID_FORMAT_EXCEPTION(400, "유효하지 않는 Type 입니다. Type을 확인 해주세요.", BAD_REQUEST),
	INVALID_INPUT_VALUE_EXCEPTION(400, "유효하지 않는 입력 값입니다.", BAD_REQUEST),
	METHOD_NOT_SUPPORT_EXCEPTION(405, "지원하지 않은 HTTP Method 입니다.", METHOD_NOT_ALLOWED),

	//JWT
	AUTHORITY_INFO_NOT_FOUND_EXCEPTION(403, "권한 정보가 없는 토큰입니다.", FORBIDDEN),
	INVALID_JWT_SIGNATURE_EXCEPTION(401,"잘못된 JWT 서명입니다.", UNAUTHORIZED),
	EXPIRED_JWT_TOKEN_EXCEPTION(401, "만료된 JWT 토큰입니다.", UNAUTHORIZED),
	UNSUPPORTED_JWT_TOKEN_EXCEPTION(401, "지원되지 않는 JWT 토큰입니다.", UNAUTHORIZED),
	INVALID_JWT_TOKEN_EXCEPTION(401, "JWT 토큰이 잘못되었습니다.", UNAUTHORIZED),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;


}
