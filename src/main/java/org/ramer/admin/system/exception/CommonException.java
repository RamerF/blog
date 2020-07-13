package org.ramer.admin.system.exception;

import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.system.entity.Constant.Txt;

/** @author ramer created on 11/15/18 */
@Slf4j
public class CommonException extends RuntimeException {
  public CommonException(final String message) {
    super(message);
    log.error(message);
  }

  public CommonException() {
    super(Txt.ERROR_SYSTEM);
    log.error(Txt.ERROR_SYSTEM);
  }

  public CommonException(final String message, final Throwable cause) {
    super(message, cause);
    log.error(message, cause);
  }

  public CommonException(final Throwable cause) {
    super(cause);
    log.error(cause.getMessage(), cause);
  }

  protected CommonException(
      final String message,
      final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    log.error(cause.getMessage(), cause);
  }
}
