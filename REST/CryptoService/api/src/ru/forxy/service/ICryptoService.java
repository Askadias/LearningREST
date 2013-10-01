package ru.forxy.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public interface ICryptoService {

    @POST
    @Path("/crypto")
    @Consumes("application/json")
    byte[] encrypt(String value);

    @POST
    @Path("/crypto")
    @Consumes("application/json")
    String decrypt(byte[] value);
}
