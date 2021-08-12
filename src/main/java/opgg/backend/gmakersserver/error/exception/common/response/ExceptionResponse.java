package opgg.backend.gmakersserver.error.exception.common.response;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.error.exception.common.ExceptionStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

	private String message;
	private String status;
	private List<ExceptionDetailResponse> errors;

	private ExceptionResponse(ExceptionStatus exceptionStatus, List<ExceptionDetailResponse> errors) {
		this.message = exceptionStatus.getMessage();
		this.status = String.valueOf(exceptionStatus.getStatus());
		this.errors = errors;
	}

	public static ExceptionResponse of(ExceptionStatus code, BindingResult bindingResult) {
		return new ExceptionResponse(code, ExceptionDetailResponse.from(bindingResult));
	}

	public static ExceptionResponse of(MethodArgumentTypeMismatchException ex) {
		String value = ex.getValue() == null ? "" : ex.getValue().toString();
		List<ExceptionDetailResponse> errors = ExceptionDetailResponse.of(ex.getName(), value, ex.getErrorCode());
		return new ExceptionResponse(ExceptionStatus.INVALID_TYPE_VALUE_EXCEPTION, errors);
	}

	public static ExceptionResponse of(InvalidFormatException ex) {
		String field = Arrays.stream(Objects.requireNonNull(ex.getTargetType().getFields()))
				.map(Field::getName)
				.collect(Collectors.joining(", "));
		String getTargetType = ex.getTargetType().toString();
		List<ExceptionDetailResponse> errors = ExceptionDetailResponse.of(
				ex.getPath().size() == 0 ? "지원 Enum = " + field : ex.getPath().get(0).getFieldName(),
				ex.getValue().toString(),
				getTargetType.contains("$") ? getTargetType.substring('$' + 1) : getTargetType);
		return new ExceptionResponse(ExceptionStatus.INVALID_FORMAT_EXCEPTION, errors);
	}

	public static ExceptionResponse of(HttpRequestMethodNotSupportedException ex) {
		String supportedMethods = Arrays.stream(Objects.requireNonNull(ex.getSupportedMethods()))
				.map(String::toString)
				.collect(Collectors.joining(", "));
		List<ExceptionDetailResponse> details = ExceptionDetailResponse.of(ex.getLocalizedMessage(),
				"입력한 HTTP Method = " + ex.getMethod(),
				"지원 가능한 HTTP Method = " + supportedMethods);
		return new ExceptionResponse(ExceptionStatus.METHOD_NOT_SUPPORT_EXCEPTION, details);
	}

}
