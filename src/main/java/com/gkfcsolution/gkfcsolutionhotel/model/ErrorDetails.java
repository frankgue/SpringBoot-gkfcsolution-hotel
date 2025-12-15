package com.gkfcsolution.gkfcsolutionhotel.model;

import java.time.LocalDateTime;

/**
 * Created on 2025 at 14:28
 * File: ErroDetails.java.java
 * Project: springboot-movies-bookinks
 *
 * @author Frank GUEKENG
 * @date 10/12/2025
 * @time 14:28
 */
public record ErrorDetails(LocalDateTime timeStamp, String message, String details, String errorCode) {

}
