package com.github.klefstad_teaching.cs122b.gateway.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.sql.Types;
import java.util.List;
import java.sql.Timestamp;

import com.github.klefstad_teaching.cs122b.gateway.request.Request;

@Component
public class GatewayRepo {

    private NamedParameterJdbcTemplate template;
    private List<Request> rq;

    @Autowired
    public GatewayRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Mono<int[]> insertRequests(List<Request> drainRequest) {

        if (drainRequest.isEmpty()) {
            return Mono.empty();
        }

        rq = drainRequest;
        return createInsertMono();

    }

    public Mono<int[]> createInsertMono() {
        return Mono.fromCallable(() -> insert());
    }

    public int[] insert() {
        MapSqlParameterSource arrayOfSources[] = createSources();

        return this.template.batchUpdate(
                "INSERT INTO gateway.request (ip_address, call_time, path) VALUES (:ip_address, :call_time, :path); ",
                arrayOfSources);
    }

    public MapSqlParameterSource[] createSources() {

        MapSqlParameterSource[] sources = rq
                .stream()
                .map(request -> new MapSqlParameterSource()
                        .addValue("ip_address", request.getIp_address(), Types.VARCHAR)
                        .addValue("call_time", Timestamp.from(request.getCall_time()), Types.TIMESTAMP)
                        .addValue("path", request.getPath(), Types.VARCHAR))
                .toArray(MapSqlParameterSource[]::new);

        return sources;

    }

}
