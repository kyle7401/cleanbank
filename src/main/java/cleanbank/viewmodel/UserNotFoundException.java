package cleanbank.viewmodel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="email 또는 비밀번호가 잘못되었습니다.")  // 404
public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1582589703847938869L;

}
