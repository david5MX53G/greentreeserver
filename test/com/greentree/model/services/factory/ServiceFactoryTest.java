package com.greentree.model.services.factory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.greentree.model.services.exception.ServiceLoadException;
import com.greentree.model.services.tokenservice.FileSystemTokenServiceImpl;
import com.greentree.model.services.tokenservice.ITokenService;

/**
 * This defines methods for testing the <code>{@link ServiceFactory}</code> class.
 * 
 * @author david.dietrich
 *
 */
public class ServiceFactoryTest {
	private static ServiceFactory factory;

	/**
	 * Instantiates a new factory used by test methods elsewhere in this class. 
	 * <code>{@link ServiceFactory#getInstance()}</code> is used to implement the Singleton Gang of
     * Four Pattern because more than one <code>ServiceFactory</code> is never necessary.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		if (ServiceFactoryTest.factory == null) {
			testGetInstance();
		}
	}
	
	/** Test method for <code>{@link ServiceFactory#getInstance()}</code>. */
	@Test
	public void testGetInstance() {
		if (ServiceFactoryTest.factory == null) {
			ServiceFactoryTest.factory = ServiceFactory.getInstance();
			assertTrue(factory != null);
			System.out.println("testGetInstance() PASSED");
		}
	}

	/** Test method for <code>{@link ServiceFactory#getTokenService()}</code>. */
	@Test
	public void testGetService() {
		ITokenService tokenService;
		try {
			tokenService = 
				(ITokenService) ServiceFactoryTest.factory.getService(ITokenService.NAME);
			assertTrue(tokenService instanceof FileSystemTokenServiceImpl);
			System.out.println("testGetService() PASSED");
		} catch (ServiceLoadException e) {
			fail("Failed with ServiceLoadException " + e.getMessage());
		}
	}

}
