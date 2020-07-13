package org.ramer.admin.system.service.common.impl;

import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.service.common.ThreadService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class ThreadServiceImpl implements ThreadService {
  private ExecutorService executorService;

  @Override
  public void newThread(Runnable runnable) {
    if (executorService == null || executorService.isShutdown()) {
      //      executorService = Executors.newFixedThreadPool(10);
      executorService =
          new ThreadPoolExecutor(
              10,
              10,
              1,
              TimeUnit.HOURS,
              new ArrayBlockingQueue<>(10),
              new ThreadPoolExecutor.CallerRunsPolicy());
    }
    try {
      executorService.execute(runnable);
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    }
  }
}
