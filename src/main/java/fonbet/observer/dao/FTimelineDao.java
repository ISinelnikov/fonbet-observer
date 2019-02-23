package fonbet.observer.dao;

import fonbet.observer.dao.base.FMatchDao;
import fonbet.observer.domain.FTimelineDto;

import java.util.Date;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class FTimelineDao extends FMatchDao {
    private static final String SQL_INSERT_TIMELINE = "insert into fonbet.timeline_table "
            + "(date_update, pair, href, start_time, c_first, c_second, f_first, cf_first, "
            + "f_second, cf_second, total, b, m) "
            + "values "
            + "(:date_update, :pair, :href, :start_time, :c_first, :c_second, :f_first, :cf_first, "
            + ":f_second, :cf_second, :total, :b, :m)";

    public FTimelineDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void addTimelineRow(FTimelineDto timelineDto) {
        MapSqlParameterSource parameterSource = extractFTimelineDto(timelineDto);
        parameterSource.addValue("date_update", new Date());

        try {
            namedTemplate.update(SQL_INSERT_TIMELINE, parameterSource);
        } catch (DataAccessException ex) {
            logger.error("Invoke method addTimelineRow({}) was failed.", timelineDto, ex);
        }
    }
}
