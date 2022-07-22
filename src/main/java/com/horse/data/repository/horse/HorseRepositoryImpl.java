package com.horse.data.repository.horse;

import com.horse.data.collection.Account;
import com.horse.data.collection.Horse;
import com.horse.data.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class HorseRepositoryImpl implements HorseCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public List<Horse> findByTrainerIdAndYear(String trainerId, Integer year) {

        Account account = accountRepository.findById(trainerId)
                .orElse(null);

        List<Horse> results = new ArrayList<>();
        AggregationOperation yearMatch = null;
        AggregationOperation yearField = null;
        if (year != 0) {
            yearField = Aggregation.addFields().addField("year").withValue(DateOperators.Year.yearOf("foaled")).build();
            yearMatch = Aggregation.match(new Criteria("year").is(year));
        }

        // search horses without trainerId
        if (account == null) {
            Aggregation aggregation = Aggregation.newAggregation(yearField, yearMatch);
            results = mongoTemplate.aggregate(aggregation, "horse", Horse.class).getMappedResults();
        } else {

            Set<String> trainerHorseId = account.getTrainerInfo().getTrainerHorses();

            for (String horseId : trainerHorseId) {
                Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(new Criteria("_id").is(horseId)));

                // check year before adding aggregation

                if (year != 0) {
                    aggregation.getPipeline().add(yearField).add(yearMatch);
                }
                AggregationResults<Horse> aggregationResults = mongoTemplate.aggregate(aggregation, "horse", Horse.class);

                if (aggregationResults.getUniqueMappedResult() != null) {
                    results.add(aggregationResults.getUniqueMappedResult());
                }
            }
        }

        return results;
    }

    @Override
    public Horse findAllByName(Horse horse, String id) {

        return null;
    }

    @Override
    public List<Horse> findAllByPrice(Integer price) {

        MatchOperation matchOperation = Aggregation.match(Criteria.where("price").is(price));

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "foaled"));

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation);

        AggregationResults output = mongoTemplate.aggregate(aggregation, "horse", Horse.class);
        return output.getMappedResults();
    }

    public AggregationOperation getHorseIdMatch(String id) {
        return Aggregation.match(new Criteria("_id").is(id));
    }

}
