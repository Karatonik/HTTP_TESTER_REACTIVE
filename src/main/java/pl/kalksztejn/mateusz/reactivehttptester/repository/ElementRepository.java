package pl.kalksztejn.mateusz.reactivehttptester.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import reactor.core.publisher.Flux;
@Repository
public interface ElementRepository extends ReactiveCrudRepository<Element, Long> {


    Flux<Element> findBy( Pageable pageable);

    Flux<Element> findElementsBySampleObjectId( Long sampleObjectId, Pageable pageable);

}
