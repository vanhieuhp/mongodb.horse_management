package com.horse.data.repository.horse;

import com.horse.data.collection.Account;
import com.horse.data.collection.Horse;
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

    @Override
    public List<Horse> findByTrainerAndYear(Account trainer, Integer year){

        List<Horse> results = new ArrayList<>();
        Set<String> trainerHorseId = trainer.getTrainerInfo().getTrainerHorses();

        for (String horseId : trainerHorseId) {
            Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(new Criteria("_id").is(horseId)),
                    Aggregation.addFields().addField("year").withValue(DateOperators.Year.yearOf("foaled")).build(),
                    Aggregation.match(new Criteria("year").is(year)));

            AggregationResults<Horse> aggregationResults = mongoTemplate.aggregate(aggregation, "horse", Horse.class);

            if (aggregationResults.getUniqueMappedResult() != null) {
                results.add(aggregationResults.getUniqueMappedResult());
            }
        }

        return results;
    }

    @Override
    public List<Horse> findByYear(Integer year) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.addFields().addField("year").withValue(DateOperators.Year.yearOf("foaled")).build(),
                Aggregation.match(new Criteria("year").is(year)));
        AggregationResults<Horse> aggregationResults = mongoTemplate.aggregate(aggregation, "horse", Horse.class);
        return aggregationResults.getMappedResults();
    }

    @Override
    public List<Horse> findByTrainer(Account trainer) {
        List<Horse> results = new ArrayList<>();
        Set<String> trainerHorseId = trainer.getTrainerInfo().getTrainerHorses();

        for (String horseId : trainerHorseId) {
            Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(new Criteria("_id").is(horseId)));
            AggregationResults<Horse> aggregationResults = mongoTemplate.aggregate(aggregation, "horse", Horse.class);
            results.add(aggregationResults.getUniqueMappedResult());
        }

        return results;
    }

    @Override
    public List<Horse> findByPrice(Integer price) {

        MatchOperation matchOperation = Aggregation.match(Criteria.where("price").is(price));

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "foaled"));

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation);

        AggregationResults<Horse> output = mongoTemplate.aggregate(aggregation, "horse", Horse.class);
        return output.getMappedResults();
    }

}
