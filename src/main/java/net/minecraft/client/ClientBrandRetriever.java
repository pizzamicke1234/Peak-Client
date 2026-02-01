package net.minecraft.client;

import peak.Client;

public class ClientBrandRetriever
{
    public static String getClientModName()
    {
        //return "vanilla";
        return Client.name;
    }
}
