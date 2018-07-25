package io.pivotal;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EnvParser {

    private final static Logger logger = LoggerFactory.getLogger(EnvParser.class);
    
    private final String PROPERTIES_VCAP_SERVICES = "VCAP_SERVICES";
    private final String SERVICE_NAME = "pgo-osb-service";

    private static EnvParser instance;

    private EnvParser() {
    }

    public static EnvParser getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (EnvParser.class) {
            if (instance == null) {
                instance = new EnvParser();
            }
        }
        return instance;
    }

    public String getUsername() throws IOException {
        Map credentials = getCredentials();
        List<Map> services = (List<Map>) credentials.get("services");
        Map service = services.get(0);
        Map spec = (Map) service.get("spec");
        List<Map> ports = (List<Map>) spec.get("ports");
        return (String) ports.get(0).get("name");
    }

    public String getPasssword() throws IOException {
        Map credentials = getCredentials();
        List<Map> secrets = (List<Map>) credentials.get("secrets");
        Map secret = secrets.get(0);
        Map data = (Map) secret.get("data");
        return (String) data.get("postgres");
    }
    
    public String getUrl() throws IOException {
        Map credentials = getCredentials();
        List<Map> services = (List<Map>) credentials.get("services");
        Map service = services.get(0);
        Map spec = (Map) service.get("spec");
        List externalIPs = (List) spec.get("externalIPs");
        String externalIP = (String) externalIPs.get(0);                
        return String.format("jdbc:postgresql://%s:5432/postgres", externalIP);
    }
    
    private Map getCredentials() throws IOException {
        Map credentials = null;
        String envContent = System.getenv().get(PROPERTIES_VCAP_SERVICES);
        ObjectMapper objectMapper = new ObjectMapper();
        Map services = objectMapper.readValue(envContent, Map.class);
        List gemfireService = getUserProvideService(services);
        if (gemfireService != null) {
            Map serviceInstance = (Map) gemfireService.get(0);
            credentials = (Map) serviceInstance.get("credentials");
        }
        return credentials;
    }
    
    private List getUserProvideService(Map services) {
        List l = (List) services.get(SERVICE_NAME);
        if (l == null) {
            throw new IllegalStateException(
                    "User Provide Service is not bound to this application");
        }
        return l;
    }
}
