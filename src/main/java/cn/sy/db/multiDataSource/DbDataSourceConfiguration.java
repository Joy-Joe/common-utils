package cn.sy.db.multiDataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DbDataSourceConfiguration extends MybatisAutoConfiguration {

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    public DbDataSourceConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        super(properties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
    }

    @Bean(name = "masterSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource masterSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "masterJdbcTemplate")
    public JdbcTemplate getMasterJdbcTemplate(@Qualifier("masterSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Override
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(dataSource());
    }

    @Bean("dataSource")
    public AbstractRoutingDataSource dataSource() {
        AbstractRoutingDataSource proxy = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DbContextHolder.DbType.MASTER, masterSource());
        proxy.setDefaultTargetDataSource(masterSource());
        proxy.setTargetDataSources(targetDataSources);
        proxy.afterPropertiesSet();
        return proxy;
    }
}
