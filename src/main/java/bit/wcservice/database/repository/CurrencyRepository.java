package bit.wcservice.database.repository;

import bit.wcservice.database.entity.datarecord.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("select currency from Currency currency where currency.date = :date")
    Currency getByDate(@Param("date") LocalDate date);

    @Query("select currency from Currency currency where currency.date between :start and :end")
    List<Currency> getCurrencyHistoryByLocation(@Param("start") LocalDate start,
                                                @Param("end") LocalDate end);

    @Query("select case when count(c) > 0 then true else false end from Currency c where c.date = :date")
    boolean existsByDate(@Param("date") LocalDate date);

    @Modifying
    @Query("delete from Currency currency where currency.date between :start and :end")
    void deleteRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
