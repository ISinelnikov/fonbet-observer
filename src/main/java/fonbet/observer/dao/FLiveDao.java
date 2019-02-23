package fonbet.observer.dao;

import fonbet.observer.dao.base.FMatchDao;
import fonbet.observer.domain.FLiveTimelineDto;

import java.util.Date;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class FLiveDao extends FMatchDao {
    private static final String SQL_INSERT_LIVE = "insert into fonbet.live_match_table "
            + "(date_update, pair, pairname, href, dop, c_first, c_second, f_first, cf_first, "
            + "f_second, cf_second, total, b, m) "
            + "values "
            + "(:date_update, :pair, :pair_name, :href, :dop, :c_first, :c_second, :f_first, :cf_first, "
            + ":f_second, :cf_second, :total, :b, :m)";

    public FLiveDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void addMatchRow(FLiveTimelineDto matchDto) {
        MapSqlParameterSource parameterSource = extractFTimelineDto(matchDto.getTimelineDto());
        parameterSource.addValue("date_update", new Date());
        parameterSource.addValue("pair_name", matchDto.getPairName());

        try {
            namedTemplate.update(SQL_INSERT_LIVE, parameterSource);
        } catch (DataAccessException ex) {
            logger.error("Invoke method addMatchRow({}) was failed.", matchDto, ex);
        }
    }
}
