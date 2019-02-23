package fonbet.observer.dao.base;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class BaseDao {
    protected static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

    protected NamedParameterJdbcTemplate namedTemplate;

    public BaseDao(JdbcTemplate jdbcTemplate) {
        this.namedTemplate = new NamedParameterJdbcTemplate(Objects
                .requireNonNull(jdbcTemplate, "Jdbc template can't be null."));
    }
}
