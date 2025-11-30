package msyc.eati.repository

import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.Commit
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("DB 연결 확인용 테스트")
class DatabaseConnectivityTest @Autowired constructor(
    private val dataSource: DataSource,
    private val jdbcTemplate: JdbcTemplate
) {

    @Test
    fun `datasource ping`() {
        val url = (dataSource as? HikariDataSource)?.jdbcUrl ?: dataSource.toString()
        println(">>>> JDBC URL = $url")
        val one = jdbcTemplate.queryForObject("select 1", Int::class.java)
        assertThat(one).isEqualTo(1)
    }

    @Test
    @Commit // 롤백 방지: 실제 DB에 흔적이 남음
    fun `insert marker row`() {
        val id = System.currentTimeMillis().toString().takeLast(8)
        jdbcTemplate.update(
            "insert into category (category_id, name, created_at, updated_at) values (?, ?, now(), now())",
            id,
            "ping-$id"
        )
        println(">>>> inserted category_id=$id")
    }
}
