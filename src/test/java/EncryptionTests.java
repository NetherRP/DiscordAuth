import fr.xen0xys.discordauth.common.encryption.Encryption;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncryptionTests {

    private final Logger logger = Logger.getLogger("EncryptionTests");
    @Test
    public void symmetricTest(){
        String key = "SecuredKey*0";
        String message = "Hello World !";
        Encryption encryption = new Encryption(this.logger);
        String encrypted = encryption.encryptSymmetric(key, message);
        String decrypted = encryption.decryptSymmetric(key, encrypted);
        assertEquals(message, decrypted);
    }

    @Test
    public void hashTest(){
        String message = "Hello World !";
        Encryption encryption = new Encryption(this.logger);
        String encrypted = encryption.hash(message);
        assertTrue(encryption.compareHash(message, encrypted));
    }
}
