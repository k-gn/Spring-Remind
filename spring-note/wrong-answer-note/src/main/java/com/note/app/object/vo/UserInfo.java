package com.note.app.object.vo;

import static org.springframework.util.StringUtils.*;

/*
	Value Object
	값은 변할 수 없다 = 불변 = final
	동일하게 생성된 VO 는 영원히 동일한 상태여야 하며, 잘못된 상태로 만들어질 수 없다.
	값이 만들어질 때 값이 유효한지 항상 확인해야 한다.
*/
public class UserInfo {

	private final long id;

	private final String username;

	private final String email;

	// 생성자는 값을 검증하고 할당하는 역할만 해야한다.
	public UserInfo(
		long id,
		String username,
		String email
	) {
		assert id > 0;
		assert hasText(username);
		// assert EmailValidator.isValid(email);
		this.id = id;
		this.username = username;
		this.email = email;
	}
}

/*
	# 그 외

	1. DTO
	- 데이터 전달에 사용되는 객체
	- 상태를 보호하지 않으며 모든 속성을 노출하므로 획득자와 설정자가 필요 없다. (public 이여도 된다는 의미)

	2. Entity
	- 유일한 식별자 존재
	- 수명주기 존재
	- 저장소에 저장하여 지속성을 가지며, 불러올 수 있다.
	- 생성자와 메서드를 사용해 인스턴스를 만들거나, 상태를 조작하는 방법을 제공
	- DB Entity 와는 다르다. (Persistence Object 가 DB Entity 라는 용어보다 적절)

	## 어떤 값을 불변으로 만들지, 어떤 인터페이스를 노출할 것인지 같은 고민들이 더 중요하다.
 */

