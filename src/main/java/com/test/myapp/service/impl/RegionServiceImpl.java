package com.test.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.test.myapp.domain.Region;
import com.test.myapp.repository.RegionRepository;
import com.test.myapp.repository.search.RegionSearchRepository;
import com.test.myapp.service.RegionService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Region}.
 */
@Service
public class RegionServiceImpl implements RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final RegionRepository regionRepository;

    private final RegionSearchRepository regionSearchRepository;

    public RegionServiceImpl(RegionRepository regionRepository, RegionSearchRepository regionSearchRepository) {
        this.regionRepository = regionRepository;
        this.regionSearchRepository = regionSearchRepository;
    }

    @Override
    public Region save(Region region) {
        log.debug("Request to save Region : {}", region);
        Region result = regionRepository.save(region);
        regionSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Region> partialUpdate(Region region) {
        log.debug("Request to partially update Region : {}", region);

        return regionRepository
            .findById(region.getId())
            .map(existingRegion -> {
                if (region.getRegionName() != null) {
                    existingRegion.setRegionName(region.getRegionName());
                }

                return existingRegion;
            })
            .map(regionRepository::save)
            .map(savedRegion -> {
                regionSearchRepository.save(savedRegion);

                return savedRegion;
            });
    }

    @Override
    public List<Region> findAll() {
        log.debug("Request to get all Regions");
        return regionRepository.findAll();
    }

    @Override
    public Optional<Region> findOne(String id) {
        log.debug("Request to get Region : {}", id);
        return regionRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Region : {}", id);
        regionRepository.deleteById(id);
        regionSearchRepository.deleteById(id);
    }

    @Override
    public List<Region> search(String query) {
        log.debug("Request to search Regions for query {}", query);
        return StreamSupport.stream(regionSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
