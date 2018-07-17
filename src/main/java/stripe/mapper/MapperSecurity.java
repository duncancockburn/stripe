package stripe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MapperSecurity {

    String CHECK_APIKEY = "select id from stripe.Users where isActive = 1 and apiKey = #{apiKey}";

    @Select(CHECK_APIKEY)
    Integer checkAPIKey(String apiKey);


}
