package opgg.backend.gmakersserver.infra.jwt;

import static opgg.backend.gmakersserver.error.exception.common.response.ExceptionResponseInfo.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opgg.backend.gmakersserver.error.exception.common.BusinessException;
import opgg.backend.gmakersserver.error.exception.jwt.ExpiredJwtTokenException;
import opgg.backend.gmakersserver.error.exception.jwt.InvalidJwtSignatureException;
import opgg.backend.gmakersserver.error.exception.jwt.InvalidJwtTokenException;
import opgg.backend.gmakersserver.error.exception.jwt.UnsupportedJwtTokenException;
import opgg.backend.gmakersserver.error.exception.common.response.ExceptionResponseInfo;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;
	private static final String ERROR_LOG_MESSAGE = "Exception = {} , message = {}";
	private static final String CONTENT_TYPE = "application/json";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (InvalidJwtSignatureException e) {
			sendErrorMessage(response, e);
		} catch (ExpiredJwtTokenException e) {
			sendErrorMessage(response, e);
		} catch (UnsupportedJwtTokenException e) {
			sendErrorMessage(response, e);
		} catch (InvalidJwtTokenException e) {
			sendErrorMessage(response, e);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
		}

	}

	private void sendErrorMessage(HttpServletResponse response, BusinessException e) throws IOException {
		response.setContentType(CONTENT_TYPE);
		response.setStatus(e.getHttpStatus().value());
		response.getWriter().write(objectMapper.writeValueAsString(from(e)));
		log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(),
				e.getLocalizedMessage());
	}

}
