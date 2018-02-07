package lansforsakringar.api;

import lansforsakringar.api.beans.*;
import lansforsakringar.api.services.RestClient;
import lansforsakringar.api.sync.SyncProperties;
import lansforsakringar.api.sync.Synchronizer;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class LansforsakringarSync {


    public static void main(String[] args) throws Exception {

        System.out.println("Starting sync");
        // Shake hands and login
        RestClient client = RestClient.createAuthenticatedClient(findProperty("CUSTOMERID"), findProperty("PINCODE"));
        Synchronizer synchronizer = new Synchronizer(client);
        SyncProperties.accounts.forEach(synchronizer);


    }


    private static String findProperty(String id) {
        String property = System.getProperty(id);
        return property;
    }

}
