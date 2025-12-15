package com.gkfcsolution.gkfcsolutionhotel.exception;

/**
 * Created on 2025 at 16:45
 * File: null.java
 * Project: springboot-movies-bookinks
 *
 * @author Frank GUEKENG
 * @date 12/12/2025
 * @time 16:45
 */
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
