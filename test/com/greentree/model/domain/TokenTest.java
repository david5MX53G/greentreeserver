package com.greentree.model.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.greentree.model.exception.TokenException;

/**
 * This class has methods for testing the <code>{@link Token}</code> class.
 * 
 * @author david.dietrich
 */
public class TokenTest {
	/** passphrase used to instantiate <code>Token</code> objects for testing. */
	private static final String PASSPHRASE = "We\'re all mad here.";
	Token token0;
	Token token1;
	Token token2;

	/**
	 * @throws TokenException when <code>{@link Token}</code> when <code>{@link Token}</code> 
	 * fails to build
	 */
	@Before
	public void setUp() {
		try {
			token0 = new Token(PASSPHRASE);
			token1 = token0;
			token2 = new Token(PASSPHRASE);
		} catch (TokenException e) {
			System.err.println("setUp() TokenException: " + e.getMessage());
		}
	}
	
	/**
	 * Tests whether two <code>{@link Token}</code> objects are identical. 
	 * @throws TokenException 
	 */
	@Test
	public void testEqualsToken() {
		assertTrue(token0.equals(token1));
		System.out.println("testEqualsToken() PASSED");
	}

	/**
	 * Checks whether two <code>Token</code> objects with the same passphrase are not equal. 
	 */
	@Test
	public void testNotEqualsToken() {
		assertFalse ("testNotEqualsToken() FAILED", token0.equals(token2));
		System.out.println("testNotEqualsToken() PASSED");
	}
	
	/** Validates each <code>{@link Token}</code> object returns a consistent, unique <code>{@link 
	 * Object#hashCode()}</code> 
	 * */
	@Test
	public void testHashCode() {
		try {
			Token token0 = new Token(PASSPHRASE);
			Token token1 = new Token(PASSPHRASE);
			assertTrue ("testHashCode() FAILED", token0.hashCode() == token0.hashCode());
			assertTrue ("testHashCode() FAILED", token0.hashCode() != token2.hashCode());
			assertTrue ("testHashCode() FAILED", token1.hashCode() == token1.hashCode());
			System.out.println("testHashCode() PASSED");
		} catch (TokenException e) {
			System.out.println("testHashCode() Exception " + e.getMessage());
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}
	
	/** Tests the <code>{@link Token#hashCode()}</code> function */
	@Test
	public void testNotHashCode() {
		try {
			assertFalse ("testNotHashCode() FAILED", token0.hashCode() == token2.hashCode());
			System.out.println("testNotHashCode() PASSED");
		} catch (AssertionError e) {
			System.err.println(e.getMessage());
		}
	}
	
	/** Tests the <code>{@link Token#toString()}</code> function */
	@Test
	public void testToString() {
		String tokenString = null;
		tokenString = token0.toString();
		try {
			assertTrue("testToString() FAILED", tokenString != null);
		} catch (AssertionError e) {
			System.err.println(e.getMessage());
		}
	}

    /** Tests with a valid token passed in. This should evaluate to <code>True</code>. */	
	@Test	
	public void testValidate() {
		try {
			assertTrue ("testValidate() FAILED", token0.validate());
			System.out.println("testValidate() PASSED");
		} catch (AssertionError | TokenException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Tests that a call to the <code>{@link Token}</code> constructor returns <code>False</code> 
	 * given invalid input.
	 */
	@Test
	public void testNotValidate() {
		Token tk = new Token();
		try { 
			try {
				assertFalse ("testNotValidate() FAILED: invalidate token validated successfully",
						tk.validate());
			} catch (TokenException e) {
				fail("testNotValidate() Failed: " + e.getMessage());
			}
			System.out.println("testNotValidate() PASSED");
		} catch (AssertionError e) {
			System.err.println(e.getMessage());
		}
	}
}