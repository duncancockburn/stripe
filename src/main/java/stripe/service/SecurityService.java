package stripe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stripe.mapper.MapperSecurity;

@Service
public class SecurityService {


    @Autowired
    MapperSecurity mapperSecurity;

    public boolean authentic(String apiKey) {
        Integer id = mapperSecurity.checkAPIKey(apiKey);
        if (id != null){
            return true;
        }
        return false;
    }
}
