package stripe.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import stripe.model.db.RefundDB;
import stripe.model.db.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperTransRef {

    String INSERT_REFUND = "INSERT INTO `stripe`.`Refunds` (`id_stripe`, `amount`, `transaction_id`) VALUES (#{id_stripe}, #{amount}, #{transaction_id})";


    String INSERT_TRANSACTIONS = "INSERT INTO `stripe`.`Transactions` (`amount`, `currency`, `source`, `receipt_email`, `id_stripe`, `amount_refunded`, `captured`) VALUES" +
            " (#{amount}, #{currency}, #{source}, #{receipt_email}, #{id_stripe}, #{amount_refunded}, #{captured})";

    String TRANSACTION_PER_ID = "select * from stripe.Transactions where id = #{arg0} or id_stripe = #{arg1}";


    String UPDATE_TRANSACTION_PER_ID = "UPDATE `stripe`.`Transactions` SET `amount_refunded`=#{arg1}, `refund`=#{arg2} WHERE `id`=#{arg0}";

    @Insert(INSERT_TRANSACTIONS)
    public int insertTransaction (Transaction trans);

    @Select(TRANSACTION_PER_ID)
    Transaction findPerTxID(int id, String id_stripe);

    @Update(UPDATE_TRANSACTION_PER_ID)
    void updateRefundAmount(Integer id, long amount, boolean refunded);

    @Insert(INSERT_REFUND)
    void insertRefund(RefundDB toBeRecordRefund);
}

