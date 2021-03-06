package com.greentree.model.business.manager;

import com.greentree.model.services.config.AppConfig;
import com.greentree.model.exception.TokenServiceException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.greentree.model.domain.Token;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * This class has methods to test the <code>{@link GreentreeManager}</code>
 * class.
 *
 * @author david5MX53G
 *
 */
public class GreenTreeManagerTest {

    /**
     * {@link GreenTreeManager} object for testing.
     */
    private static GreenTreeManager mngr;

    /**
     * {@link RSAPublicKey} for testing <code>Token</code>.
     */
    private static RSAPublicKey key;

    /**
     * Passphrase for managing {@link Token} objects.
     */
    private static final String PASS = "More pepper!";

    /**
     * "Since naming Loggers after their owning class is such a common idiom,
     * the convenience method LogManager.getLogger() is provided to
     * automatically use the calling class's fully qualified class name as the
     * Logger name." (Apache Software Foundation, "Log4j – Log4j 2 Architecture
     * - Apache Log4j 2", "Retrieving Loggers" sectio
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * This ensures that {@link GreenTreeManager} is initialized before every
     * test in this class.
     */
    @Before
    public void setUp() {
        if (mngr instanceof GreenTreeManager == false) {
            getBeanTest();
        }
    }

    /**
     * Instantiate the {@link GreenTreeManager} singleton using the Spring
     * Inversion of Control (IoC) Container
     * <a href="https://docs.spring.io/spring/docs/5.1.0.RC2/spring-framework-reference/core.html#beans-java-instantiating-container">
     * AnnotationConfigApplicationContext</a>.
     */
    @Test
    public void getBeanTest() {
        String methodName = "getBeanTest()";
        try {
            ApplicationContext ctx
                = new FileSystemXmlApplicationContext("config/applicationContext.xml");

            mngr = ctx.getBean(GreenTreeManager.class);

            assertTrue(methodName + " FAILED", mngr instanceof GreenTreeManager);

            LOG.debug(methodName + " PASSED");
        } catch (BeansException e) {
            LOG.error(methodName + " threw " + e.getClass().getSimpleName() 
                + ": " + e.getMessage());
        }
    }

    /**
     * Tests {@link GreenTreeManager#loadProperties()}
     */
    @Test
    public void loadPropertiesTest() {
        try {
            GreenTreeManager.loadProperties();
            LOG.debug("loadPropertiesTest() PASSED");
        } catch (Exception e) {
            fail("loadPropertiesTest() threw PropertyFileNotFoundException: " + e.getMessage());
        }
    }

    /**
     * Tests {@link GreenTreeManager#registerService(String)}
     */
    @Test
    public void registerServiceTest() {
        if (GreenTreeManagerTest.mngr.getTokenService() == null) {
            try {
                GreenTreeManagerTest.mngr.registerService("TokenService");
                assertTrue(GreenTreeManagerTest.mngr.getTokenService() != null);
                LOG.debug("registerServiceTest() PASSED");
            } catch (AssertionError e) {
                fail("registerServiceTest() " + e.getClass().getSimpleName()
                    + e.getMessage());
            }
        }
    }

    /**
     * Tests {@link GreenTreeManager#registerToken(String)}
     */
    @Test
    public void registerTokenTest() {
        if (GreenTreeManagerTest.key == null) {
            mngr.registerToken(GreenTreeManagerTest.PASS);
            GreenTreeManagerTest.key = mngr.getPublicKey();
            assertTrue(GreenTreeManagerTest.key != null);
            LOG.debug("registerTokenTest() PASSED");
        }
    }

    /**
     * Tests {@link GreenTreeManager#logOut()}
     */
    @Test
    public void logOutTest() {
        try {
            // logOut() requires the GreenTreeManager to have a Token
            if (GreenTreeManagerTest.key == null) {
                registerTokenTest();
            }

            mngr.logOut();
            assertTrue(true);
            LOG.debug("logOutTest() PASSED");
        } catch (TokenServiceException e) {
            fail("logOutTest() " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

    }

    /**
     * Tests {@link GreenTreeManager#auth(RSAPublicKey, String)}
     */
    @Test
    public void authTest() {
        Cipher cipher;
        String ciphertext = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ciphertext = Base64.encodeBase64String(
                cipher.doFinal(PASS.getBytes("UTF-8"))
            );

            if (ciphertext != null) {
                mngr.registerToken(key, ciphertext);
                assertTrue(
                    mngr.getPublicKey() instanceof RSAPublicKey
                );
                LOG.debug("authTest() PASSED");
            } else {
                fail("authTest() FAILED encrypting pass");
            }
        } catch (UnsupportedEncodingException | InvalidKeyException
            | NoSuchAlgorithmException | BadPaddingException
            | IllegalBlockSizeException | NoSuchPaddingException e) {
            fail("authTest() "
                + e.getClass().getSimpleName()
                + ": "
                + e.getMessage());
            LOG.error("authTest() FAILED");
        }
    }

    /**
     * Tests {@link
     * GreenTreeManager#getData(java.security.interfaces.RSAPublicKey)}
     */
    @Test
    public void getDataTest() {
        // getData(RSAPublicKey) requires the GreenTreeManager to have a Token
        if (key == null) {
            registerTokenTest();
        }

        ArrayList<String> aList = mngr.getData(key);

        aList.stream()
            .map(s -> s.length() + ": " + s)
            .forEach(t -> LOG.info(t));

        assertTrue(aList instanceof ArrayList);
        LOG.info("getDataTest() PASSED");
    }
}
