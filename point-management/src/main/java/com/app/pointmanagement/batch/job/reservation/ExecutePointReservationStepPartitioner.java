package com.app.pointmanagement.batch.job.reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import com.app.pointmanagement.domain.point.reservation.PointReservationRepository;

import lombok.RequiredArgsConstructor;

/*
	# 파티셔닝
		- 처리할 데이터를 더 작게 나누어 처리한다.
		- 멀티 스레드 또는 여러 서버가 동시에 처리한다.
		- MasterStep -> WorkerStep
		- 동시성 문제 주의

	https://github.dev/kker5/point-management-practice/tree/partition 참고
 */
@RequiredArgsConstructor
public class ExecutePointReservationStepPartitioner implements Partitioner {
	private final PointReservationRepository pointReservationRepository;
	private final LocalDate today;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		long min = pointReservationRepository.findMinId(today);
		long max = pointReservationRepository.findMaxId(today);
		long targetSize = (max - min) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<>();
		long number = 0;
		long start = min;
		long end = start + targetSize - 1;

		while (start <= max) {
			ExecutionContext value = new ExecutionContext();
			if (end >= max) {
				end = max;
			}
			value.putLong("minId", start);
			value.putLong("maxId", end);
			result.put("partition" + number, value);
			start += targetSize;
			end += targetSize;
			number++;
		}
		return result;
	}
}
