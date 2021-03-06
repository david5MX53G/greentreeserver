/*
 * The MIT License
 *
 * Copyright 2018 david5MX53G.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.greentree.model.exception;

import org.apache.logging.log4j.Logger;

/**
 * This class provides a way to conveniently log the {@link Exception} class 
 * name and message to {@link org.apache.logging.log4j} while passing the 
 * <code>Exception</code> on to the super constructor.
 * 
 * @author david5MX53G
 */
public class GreenTreeException extends Exception {
    /**
     * Throws a new {@link Exception} and logs a message.
     * 
     * @param message {@link String} for logging
     * @param logger {@link org.apache.logging.log4j.Logger} for writing the msg
     * @param ex {@link Throwable} for logging the original {@link Exception} 
     * class name
     */
    public GreenTreeException(String message, Logger logger, Throwable ex) {        
        super(ex.getClass().getSimpleName() + " " + ex.getMessage());
        logger.error(ex.getMessage());
    }
}
