package com.mayu.practice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.Semaphore;

/**
 * @Author: 马瑜
 * @Date: 2024/8/19 10:27
 * @Description:
 */
@Slf4j
@Component
public class FtpConfig {

    private static Semaphore semaphore;
    @Value("${jk.qualityTask.semaphore}")
    public void setSemaphore(Integer size) {
        semaphore = new Semaphore(size);
    }

    private Runnable getRunnable(Semaphore semaphore){
        return () -> {
            try {
                log.info("======:{}", semaphore.availablePermits());
            } finally {
                semaphore.release();
            }
        };
    }

    public void doHandle() {
        Runnable runnable = getRunnable(semaphore);
        try {
            semaphore.acquire();
            new Thread(runnable).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
