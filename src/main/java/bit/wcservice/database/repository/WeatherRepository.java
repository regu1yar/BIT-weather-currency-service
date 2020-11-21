package bit.wcservice.database.repository;

import bit.wcservice.database.entity.datarecord.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("select weather from Weather weather where weather.date = :date and weather.location = :location")
    Weather getByDateAndLocation(@Param("date") LocalDate date, @Param("location") String location);

    @Query("select weather from Weather weather where weather.location = :location and weather.date between :start and :end")
    List<Weather> getWeatherHistoryByLocation(@Param("start") LocalDate start,
                                              @Param("end") LocalDate end,
                                              @Param("location") String location);

    @Query("select case when count(w) > 0 then true else false end from Weather w where w.date = :date and w.location = :location")
    boolean existsByDateAndLocation(@Param("date") LocalDate date, @Param("location") String location);

    @Modifying
    @Query("delete from Weather weather where weather.location = :location and weather.date between :start and :end")
    void deleteRangeByLocation(@Param("start") LocalDate start,
                               @Param("end") LocalDate end,
                               @Param("location") String location);

}
