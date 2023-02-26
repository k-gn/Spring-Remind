package com.note.app.demeter;

import java.util.List;

import lombok.Getter;

@Getter
public class Computer {

	private List<Disk> disks;

	public long getDiskSize() {
		return disks.stream().map(Disk::getSize).reduce(0L, Long::sum);
	}

	public void printDiskSize() {
		System.out.println(this.getDiskSize());
	}
}
