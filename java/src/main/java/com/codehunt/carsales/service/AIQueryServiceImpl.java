package com.codehunt.carsales.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AIQueryServiceImpl implements AIQueryService {

    private final JdbcTemplate jdbcTemplate;

    public AIQueryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String process(String question) {

        String sql = generateSQL(question);

        System.out.println(sql);

        if ("INVALID".equalsIgnoreCase(sql)) {
            return "Only car sales related questions are allowed.";
        }

        if (!isSafe(sql)) {
            return "Unsafe query.";
        }

        try {

            List<Map<String, Object>> result =
                    jdbcTemplate.queryForList(sql);

            if (result.isEmpty()) {
                return "No data found.";
            }

            return result.toString();

        } catch (Exception e) {

            return "Query failed : " + e.getMessage();

        }
    }

    private boolean isSafe(String sql) {

        String lower = sql.toLowerCase();

        return lower.startsWith("select")
                && !lower.contains("delete")
                && !lower.contains("drop")
                && !lower.contains("update")
                && !lower.contains("insert");
    }

    // Keep ONLY one generateSQL() method
    private String generateSQL(String question) {

        String q = question.toLowerCase();

        if (q.contains("count") || q.contains("total")) {

            return "select count(*) as total_sales from car_sales";

        }

        if (q.contains("year")) {

            return """
                    select YEAR(date_of_purchase) as year,
                           count(*) as total
                    from car_sales
                    group by YEAR(date_of_purchase)
                    order by YEAR(date_of_purchase)
                    """;

        }

        if (q.contains("brand")) {

            return """
                    select brand,
                           count(*) as total
                    from car_sales
                    group by brand
                    """;

        }

        return "INVALID";
    }

}