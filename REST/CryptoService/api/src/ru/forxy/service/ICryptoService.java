package ru.forxy.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public interface ICryptoService {

    @GET
    @Path("/encrypt")
    byte[] encrypt(String decrypted);

    @POST
    @Path("/decrypt")
    String decrypt(byte[] encrypted);

    @POST
    @Path("/hash")
    byte[] hash(String value);
}
