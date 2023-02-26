package com.note.app.demeter;

/*
	# 디미터의 법칙
	- 최소 지식의 법칙
	- 모듈은 자신이 조작하는 개체의 속사정을 몰라야한다.

 */
public class ComputerManager {

	public void printSpec(Computer computer) {
		/* # Bad
			long size = 0;
			for (int i = 0; i < computer.getDisks().size(); i++) {
				size += computer.getDisks().get(i).getSize();
			}
		*/
		
		// Good
		computer.getDiskSize();
	}

}
