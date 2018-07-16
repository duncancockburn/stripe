package stripe.mapper;

import stripe.model.db.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperTransRef {

    String INSERT_ARTICLE = "INSERT INTO `stripe`.`Transactions` (`amount`, `currency`, `source`, `receipt_email`, `id_stripe`, `amount_refunded`, `captured`) VALUES" +
            " (#{amount}, #{currency}, #{source}, #{receipt_email}, #{id_stripe}, #{amount_refunded}, #{captured})";

    @Insert(INSERT_ARTICLE)
    public int insertTransaction (Transaction trans);

}

