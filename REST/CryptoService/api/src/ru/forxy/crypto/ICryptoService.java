package ru.forxy.crypto;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public interface ICryptoService {

    @POST
    @Path("/encrypt")
    byte[] encrypt(String decrypted);

    @POST
    @Path("/decrypt")
    String decrypt(byte[] encrypted);

    @POST
    @Path("/hash")
    byte[] hash(String value);
}
