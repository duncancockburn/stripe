package stripe.mapper;

import org.apache.ibatis.annotations.Select;
import stripe.model.db.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperTransRef {

    String INSERT_ARTICLE = "INSERT INTO `stripe`.`Transactions` (`amount`, `currency`, `source`, `receipt_email`, `id_stripe`, `amount_refunded`, `captured`) VALUES" +
            " (#{amount}, #{currency}, #{source}, #{receipt_email}, #{id_stripe}, #{amount_refunded}, #{captured})";

    String TRANSACTION_PER_ID = "select * from stripe.Transactions where id = #{arg0} or id_stripe = #{arg1}";

    @Insert(INSERT_ARTICLE)
    public int insertTransaction (Transaction trans);

    @Select(TRANSACTION_PER_ID)
    Transaction findPerTxID(int id, String id_stripe);
}

