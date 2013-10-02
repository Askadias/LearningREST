package ru.forxy.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public interface ICryptoService {

    @POST
    @Path("/crypto")
    byte[] encrypt(String value);

    @POST
    @Path("/crypto")
    String decrypt(byte[] value);
}
