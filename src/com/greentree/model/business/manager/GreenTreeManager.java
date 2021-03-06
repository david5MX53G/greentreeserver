/**
 * This package defines the business management layer of the GreenTree application.
 */
package com.greentree.model.business.manager;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.greentree.model.exception.TokenServiceException;
import com.greentree.model.domain.Block;
import com.greentree.model.domain.Claim;
import com.greentree.model.domain.Token;
import com.greentree.model.exception.ServiceLoadException;
import com.greentree.model.services.factory.ServiceFactory;
import com.greentree.model.services.manager.JDBCPoolManager;
import com.greentree.model.services.tokenservice.ITokenService;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GreenTreeManager defines methods used by the presentation layer to manage
 * <code>{@link Token}</code> objects with associated <code>{@link Block}</code>
 * and <code>{@link Claim}</code> objects.
 *
 * @author david5MX53G
 *
 */
public class GreenTreeManager extends ManagerSuperType {
    /**
     * This {@link org.apache.logging.log4j.Logger} is good for logging!
     */
    private static final Logger LOG = LogManager.getLogger();
    
    /** 
     * <code>{@link ServiceFactory}</code> for building services.
     */
    private final ServiceFactory factory = ServiceFactory.getInstance();

    /**
     * stores the <code>{@link Token}</code> for the session
     */
    private Token token = null;

    /**
     * stores the encrypted passphrase of the <code>Token</code> for this
     * session
     */
    private String ciphertext;

    /**
     * Stores the <code>{@link ITokenService}</code> for storing and retrieving
     * <code>{@link Token}</code> objects
     */
    private ITokenService tokenService;

    /**
     * Stores the singleton instance of this class.
     */
    private static GreenTreeManager _instance;

    /**
     * constructs the Singleton instance of <code>GreenTreeManager</code>. This 
     * triggers the static init block in {@link ManagerSuperType}.
     */
    private GreenTreeManager() {}

    /**
     * Instantiates a new {@link GreenTreeManager} instance using the Singleton
     * Design Pattern.
     *
     * @return the <code>GreenTreeManager</code> Singleton instance
     */
    public static GreenTreeManager getInstance() {
        if (_instance == null) {
            _instance = new GreenTreeManager();
        }
        LOG.debug("GreenTreeManager getInstance() returned _instance");
        return _instance;
    }
    
    /**
     * This sets the {@link ITokenService} member of the class. This member is 
     * used to create or modify {@link Token} objects.
     * 
     * @param tokenService to set on this class
     */
    public void setTokenService (ITokenService tokenService) {
        this.tokenService = tokenService;
        LOG.debug("setTokenService (ITokenService) " 
            + (this.tokenService instanceof ITokenService));
    }

    /**
     * Instantiates a new <code>{@link ITokenService}</code> for use by methods
     * in this class.
     *
     * @throws ServiceLoadException when the <code>{@link ITokenService}</code>
     * fails to load
     */
    private boolean registerTokenService() {
        boolean success = false;
        try {
            this.tokenService = (ITokenService) factory.getService(ITokenService.NAME);
            success = true;
        } catch (ServiceLoadException e) {
            String msg = "ServiceFactory failed to get " + ITokenService.NAME + ": "
                + e.getMessage();
            LOG.debug(this.getClass().getSimpleName() + ": "
                + e.getClass().getSimpleName() + ": " + msg);
        }
        return success;
    }

    /**
     * Instantiates a <code>{@link Token}</code>, adds an initial
     * <code>{@link Block}</code> to it, commits it to storage via
     * <code>{@link ITokenService}</code>, and saves it to this object.
     *
     * @param plaintext passphrase with which to create the <code>Token</code>
     * @return boolean indicates whether the method was successful or not
     */
    public boolean registerToken(String plaintext) {
        boolean success;

        if (this.tokenService == null) {
            this.registerService("TokenService");
        }

        try {
            this.token = new Token(plaintext);
            
            if (this.token instanceof Token) {
                success = this.getTokenService().commit(this.token);
                this.ciphertext = token.encrypt(plaintext);
            } else {
                LOG.error("registerToken(" + plaintext + ") "
                    + "failed to initialize Token");
                success = false;
            }
        } catch (TokenServiceException e) {
            LOG.error(
                e.getClass().getSimpleName() + ": "
                + e.getMessage()
            );
            success = false;
        }
        
        return success;
    }

    /**
     * Logs a logout event to the <code>{@link Token}</code>, saves it with <code>
     * {@link ITokenService}</code>, and removes it from this object.
     *
     * @throws TokenServiceException when the input <code>Token</code> is bad
     */
    public void logOut() throws TokenServiceException {
        String dateStamp = new Date().toString();
        this.token.addBlock("logged out at " + dateStamp, this.ciphertext);
        try {
            getTokenService().commit(token);
            LOG.debug("Token commit done");
            this.token = null;
            LOG.debug("Token is null");
            JDBCPoolManager.shutDown();
            LOG.debug("JDBC pool shut down");
        } catch (TokenServiceException | SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Retrieves the <code>Token</code> corresponding to the given key,
     * validates the passphrase, and registers the <code>Token</code> in the
     * current {@link GreenTreeManager} instance, if the passphrase is valid.
     *
     * @param key used to locate the <code>Token</code> and decrypt the
     * passphrase
     * 
     * @param ciphertext used to authenticate ownership of the
     * <code>Token</code>
     * 
     * @return {@link boolean} for success (true) or failure (false)
     */
    public boolean registerToken(RSAPublicKey key, String ciphertext) {
        boolean success = registerService("TokenService");
        LOG.debug(
            "registerService(\"TokenServce\") returned "
            + String.valueOf(success)
        );
        if (!success) {
            LOG.debug("registerService(\"TokenService\") FAILED");
        } else {
            try {
                this.token = this.tokenService.selectToken(key);
                if (this.token == null) {
                    LOG.error("getTokenService().selectToken(key) FAILED");
                } else if (token.checkPassphrase(ciphertext)) {
                    LOG.debug("token.checkPassphrase(ciphertext) is true");
                    this.ciphertext = ciphertext;
                    token.addBlock(
                        "authenticated at " + new Date().toString(), ciphertext
                    );
                    tokenService.commit(token);
                    success = true;
                } else {
                    LOG.debug("token.checkPassphrase(ciphertext) is false");
                    success = false;
                }
            } catch (TokenServiceException e) {
                LOG.error(e.getMessage());
                success = false;
            }
        }
        return success;
    }

    /**
     * retrieves an {@link IService} implementation
     *
     * @return true, if registration was successful
     */
    @Override
    public boolean registerService(String name) {
        boolean success = false;
        if (name.equalsIgnoreCase("TokenService")) {
            success = this.registerTokenService();
            if (!success) {
                String msg
                    = this.getClass().getSimpleName() + ": registerTokenService() failed";
                LOG.debug(msg);
            } else {
                success = true;
            }
        }
        return success;
    }

    /**
     * Instantiates a <code>{@link Token}</code>, adds an initial
     * <code>{@link Block}</code>, commits it to storage via
     * <code>{@link ITokenService}</code>, and saves it to this object. This
     * uses <code>char[]</code> for input rather than
     * <code>{@link String}</code> because the latter persists in memory longer
     * than the former, increasing the window of opportunity for an attacker
     * with read access to copy the pass in plaintext. See Stackoverflow <a href=
     * "https://stackoverflow.com/questions/8881291/why-is-char-preferred-over-string-for-passwords">
     * 8881291</a>.
     *
     * @param pass <code>{@link java.lang.Character}</code>
     * <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html">
     * array</a> for building a new <code>Token</code>
     */
    public void registerToken(char[] pass) {
        // TODO Auto-generated method stub

    }

    /**
     * @return {@link RSAPublicKey} for the {@link Token} registered to this
     * {@link GreenTreeManager}
     */
    public RSAPublicKey getPublicKey() {
        return this.token.getPublicKey();
    }

    /**
     * Returns the data from each <code>Block</code> in the block chain of the
     * given <code>Token
     * </code> where the <code>Block</code> contains at least one
     * <code>Claim</code> valid for the requesting <code>Token</code>.
     *
     * @param key {@link RSAPublicKey} identifying the {@link Token} from which
     * to retrieve data. Only data will be returned which its <code>Token</code>
     * has authorized via {@link Claim} objects for the active
     * {@link GreenTreeManager} <code>Token</code>.
     * @return {@link ArrayList}<{@link String}> obtained from the {@link com.greentree.model.
     *     domain.Block} objects in the <code>blockChain</code> of the given {@link com.greentree.
     *     model.domain.Token} where at least one
     * {@link com.greentree.model.domain.Claim} exists matching the
     * <code>Token</code> of the active <code>GreenTreeManager</code>.
     */
    public ArrayList<String> getData(RSAPublicKey key) {
        String methodName = "public ArrayList<String> getData(RSAPublicKey)";
        ArrayList<String> stringData = new ArrayList<>();
        if (this.token == null) {
            LOG.error(methodName + " missing token for given key");
        } else {
            try {
                // get the Token for the given RSAPublicKey
                Token keyToken = getTokenService().selectToken(key);
                
                // get the list of Block objects for the given Token
                ArrayList<Block> list = keyToken.getBlockChain();
                
                // set the current time
                final Long start = new GregorianCalendar().getTimeInMillis();
                
                // get the data of each Block, assigning Claims as needed
                list.stream()
                   .map(blck -> {
                       if (keyToken.equals(this.token)) { 
                           blck.addClaim(
                               new Claim(this.token, start, start + 60000), 
                               this.ciphertext
                           );
                       }
                       return blck.getData(token, this.ciphertext);
                   })
                   .forEach(str -> {
                       if (str != null) {
                           stringData.add(str);
                       }
                   });
            } catch (TokenServiceException e) {
                LOG.error(e.getClass().getSimpleName() + " " + e.getMessage());
            }
        }
        return stringData;
    }

    /**
     * adds a {@link Block} to the active <code>Token</code> with a
     * {@link Claim} granting access to another <code>Token</code> for the given
     * duration.
     *
     * @param data {@link String} to be stored in the new <code>Block</code>
     * @param clientKey {@link RSAPublicKey} identifying the {@link Token} that
     * will have access to this <code>Block</code>
     * @param notBefore <code>long</code> specifies the earliest time in the
     * number of millis since January 1, 1970, 00:00:00 GMT that access to this
     * <code>Block</code> will be allowed
     * @param notAfter <code>long</code> specifies the time after which
     * access to this <code>
     *     Block</code> will be denied in the number of millis since January 1,
     * 1970, 00:00:00 GMT
     * @return {@link Boolean} true, when execution is successful; otherwise,
     * false
     */
    public boolean addBlock(String data, RSAPublicKey clientKey, long notBefore,
        long notAfter) {
        boolean result;
        try {
            Token clientToken = getTokenService().selectToken(clientKey);

            Claim claim = new Claim(clientToken, notBefore, notAfter);

            this.token.addBlock(data, this.ciphertext, claim);
            getTokenService().commit(this.token);

            LOG.debug("getTokenService().commit(this.token) PASSED");
            result = true;
        } catch (TokenServiceException e) {
            LOG.debug(this.getClass().getSimpleName() + ": " + e.getClass().getName()
                + ": " + e.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * removes the {@link Token} from the current {@link GreenTreeManager}
     * instance. This was originally done purely for testing the overloaded
     * {@link GreenTreeManager#registerToken()} method.
     */
    public void deregisterToken() {
        this.token = null;
    }

    /**
     * @return {@link ITokenService} tokenService
     */
    public ITokenService getTokenService() {
        return tokenService;
    }
}
