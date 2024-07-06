package org.example.utils.es;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import lombok.Data;

import java.util.Map;

@Data
public class VectorQueryTemplate {

    private Query query;



    public static class Query {

        private FunctionScore function_score;

    }

}
