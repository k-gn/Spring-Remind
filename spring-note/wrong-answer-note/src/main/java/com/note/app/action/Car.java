package com.note.app.action;

import java.util.List;

public class Car {

	// 데이터 위주의 사고 (Struct)
	private Frame frame;
	private Engine engine;
	private Speed speed;
	private List<Wheel> wheels;

	// 행동 위주의 사고 (Class - 객체지향적)
	public void drive() {}
	public void changeDirection() {}
	public void accelerate(Speed speed) {}
	public void decelerate(Speed speed) {}

	/*
		# 순환참조, 양방향 참조는 만들지 말자.
		- 간혹 필요하지만, 신중을 기하자.
		- 순환 의존성 자체가 높은 결합도롤 의미
		- 직렬화가 불가능하다.
			- 회피방법이 존재하나, 부자연스럽다. (결과가 이상함)

		# 해결방법
		- 간접 참조 (id 를 들고있는 방식)
		- 별도의 클래스로 컴포넌트 분리 검토 (A <-> B => A -> C, B -> C)

		# 기타
		- final 이어야하는지, 이름은 뭐가 좋을지 고민하는 습관은 좋은습관!
		- VO 의 변경자 -> 새로운 VO 반환, VO 변경자 이름 (changePassword < withNewPassword)

		# 여담
		- 도메인 엔티티와 영속성 엔티티를 구분하는 방식도 존재한다.
	 */
}
