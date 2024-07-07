package org.example.knowledge.rerank;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.DocUnit;
import org.example.domain.SearchedDocUnitResult;
import org.example.enums.RetrievalType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class SimpleSearchedReranker implements ISearchedReranker {


    private static final Integer TOP_N = 3;


    @Override
    public <T extends DocUnit> List<SearchedDocUnitResult<T>> rerank(List<SearchedDocUnitResult<T>> list) {

        List<SearchedDocUnitResult<T>> copyedList = List.copyOf(list);

        List<SearchedDocUnitResult<T>> matchResultList = copyedList.stream().filter((s) -> RetrievalType.MATCH.equals(s.getRetrievalType())).toList();

        List<SearchedDocUnitResult<T>> scoredList = copyedList.stream().filter((s) -> RetrievalType.VECTOR.equals(s.getRetrievalType())).peek((s) -> {
            double score = s.getScore();
            SearchedDocUnitResult<T> sameResult = matchResultList.stream().filter((m) -> m.getDocUnit().getId().equals(s.getDocUnit().getId())).findAny().orElse(null);
            if (sameResult != null) {
                score +=  Math.sqrt(sameResult.getScore());
            }
            s.setScore(score);
        }).toList();

        List<SearchedDocUnitResult<T>> sortedList = scoredList.stream()
                .sorted(Comparator.comparing(SearchedDocUnitResult::getScore, Comparator.reverseOrder()))
                .limit(TOP_N)
                .toList();

        return sortedList;
    }


}
