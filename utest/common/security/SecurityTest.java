package common.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityTest {
    @Test
    public void testSuccessGeneratePasswordHash(){
        String result = Security.GeneratePasswordHash("password");
        assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", result);
    }
    @Test
    public void testEmpty(){
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Security.GeneratePasswordHash(""));
    }
    @Test
    public void testLongPasswordLength(){
        assertEquals("7ae25a72b71a42ccbc5477fd989cd512", Security.GeneratePasswordHash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
}