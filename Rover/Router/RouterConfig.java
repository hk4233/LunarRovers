package Rover.Router;

import java.util.*;

public final class RouterConfig {
    int port = 1337;
    String multi_IP = "224.0.0.9";
    List<String> AddressTemplate = Arrays.asList("10", "0", "-1", "0");
    String current_addr;
    byte router_ID;
    

    public RouterConfig(byte router_ID) {
        this.current_addr = getCurrentAddress();
        this.router_ID = router_ID;
        
    }

    public String getCurrentAddress () {
        for(String st : AddressTemplate){
            if(st.equals("-1")) st = Byte.toString(this.router_ID);
        }
        return String.join(".", AddressTemplate);
    }

    public int getPort() {
        return port;
    }
}
