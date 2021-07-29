package opgg.backend.gmakersserver.error.exception.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDetailResponse {

	private final String field;
	private final String value;
	private final String reason;

	public static List<ErrorDetailResponse> of(String field, String value, String reason) {
		List<ErrorDetailResponse> fieldErrors = new ArrayList<>();
		fieldErrors.add(new ErrorDetailResponse(field, value, reason));
		return fieldErrors;
	}

	public static List<ErrorDetailResponse> from(BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		return fieldErrors.stream()
			.map(error -> new ErrorDetailResponse(
				error.getField(),
				error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
				error.getDefaultMessage() == null ? "" : toDefaultMessage(error)))
			.collect(Collectors.toList());
	}

	public static String toDefaultMessage(FieldError error) {
		String message = Objects.requireNonNull(error.getDefaultMessage());
		String resultMessage;
		if (message.contains("No enum")) {
			resultMessage = "Enum Type 이 일치하지 않습니다.";
		} else {
			resultMessage = error.getDefaultMessage();
		}
		return resultMessage;
	}
}

