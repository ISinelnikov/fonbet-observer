package fonbet.observer.dao.base;

import fonbet.observer.dao.base.BaseDao;
import fonbet.observer.domain.FTimelineDto;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class FMatchDao extends BaseDao {
    public FMatchDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    protected MapSqlParameterSource extractFTimelineDto(FTimelineDto timelineDto) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        parameterSource.addValue("pair", timelineDto.getPair());
        parameterSource.addValue("href", timelineDto.getHref());
        parameterSource.addValue("start_time", timelineDto.getStartTime());
        parameterSource.addValue("c_first", timelineDto.getCFirst());
        parameterSource.addValue("c_second", timelineDto.getCSecond());
        parameterSource.addValue("f_first", timelineDto.getFFirst());
        parameterSource.addValue("cf_first", timelineDto.getCFFirst());
        parameterSource.addValue("f_second", timelineDto.getFSecond());
        parameterSource.addValue("cf_second", timelineDto.getCFSecond());
        parameterSource.addValue("total", timelineDto.getTotal());
        parameterSource.addValue("b", timelineDto.getB());
        parameterSource.addValue("m", timelineDto.getM());

        return parameterSource;
    }
}
