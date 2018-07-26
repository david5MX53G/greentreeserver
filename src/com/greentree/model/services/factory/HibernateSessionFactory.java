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
package com.greentree.model.services.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This class is a {@link org.hibernate.SessionFactory} 
 * implementation that builds and returns {@link org.hibernate.Session} 
 * instances.
 * 
 * @author david5MX53G
 */
public class HibernateSessionFactory {
    /**
     * This {@link org.apache.logging.log4j.Logger} is for log messaging.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * This {@link java.lang.ThreadLocal} stores the 
     * {@link org.hibernate.Session} Singleton.
     */
    private static final ThreadLocal threadLocal = new ThreadLocal();
    
    /**
     * This {@link org.hibernate.SessionFactory} will create 
     * {@link org.hibernate.Session} instances.
     */
    private static org.hibernate.SessionFactory sessionFactory;
    
    /**
     * This returns the Hibernate <code>Session</code> Singleton.
     * 
     * @return {@link org.hibernate.Session} Singleton
     * @throws HibernateException 
     */
    public static Session currentSession() throws HibernateException {
        throw new NotImplementedException();
    }
    
    /**
     * Runs {@link java.io.Closeable#close()} on the 
     * {@link org.hibernate.Session} Singleton
     * 
     * @throws HibernateException 
     */
    public static void closeSession() throws HibernateException {
        throw new NotImplementedException();
    }
    
    /**
     * Closes the {@link org.hibernate.SessionFactory} of this class
     */
    public static void closeFactory() throws HibernateException {
        throw new NotImplementedException();
    }
    
    /**
     * Private constructor for building the {@link HibernateSessionFactory} 
     * Singleton
     */
    private HibernateSessionFactory() {}
}
