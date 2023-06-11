package com.example.api.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@SpringBootTest
// @Transactional // 부트 @Transactionl 은 일반적으로 단일쓰레드에서만 지원
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponCountRepository couponCountRepository;

	@BeforeEach
	public void teardown() {
		couponCountRepository.init();
	}

	@Test
	void 한번만응모() {
		applyService.apply(1L);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	@Test
	void 여러명응모() throws InterruptedException {
		int threadCount = 1000;

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(userId);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();

		// 쿠폰 생성까지 약간의 term이 존재한다.
		Thread.sleep(10000);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(100);
	}

	@Test
	void 한명당_한개의쿠폰만_발급() throws InterruptedException {
		int threadCount = 1000;

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					applyService.apply(1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();

		Thread.sleep(10000);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}
}