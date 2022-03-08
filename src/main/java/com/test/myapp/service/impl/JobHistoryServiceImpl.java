package com.test.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.test.myapp.domain.JobHistory;
import com.test.myapp.repository.JobHistoryRepository;
import com.test.myapp.repository.search.JobHistorySearchRepository;
import com.test.myapp.service.JobHistoryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link JobHistory}.
 */
@Service
public class JobHistoryServiceImpl implements JobHistoryService {

    private final Logger log = LoggerFactory.getLogger(JobHistoryServiceImpl.class);

    private final JobHistoryRepository jobHistoryRepository;

    private final JobHistorySearchRepository jobHistorySearchRepository;

    public JobHistoryServiceImpl(JobHistoryRepository jobHistoryRepository, JobHistorySearchRepository jobHistorySearchRepository) {
        this.jobHistoryRepository = jobHistoryRepository;
        this.jobHistorySearchRepository = jobHistorySearchRepository;
    }

    @Override
    public JobHistory save(JobHistory jobHistory) {
        log.debug("Request to save JobHistory : {}", jobHistory);
        JobHistory result = jobHistoryRepository.save(jobHistory);
        jobHistorySearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<JobHistory> partialUpdate(JobHistory jobHistory) {
        log.debug("Request to partially update JobHistory : {}", jobHistory);

        return jobHistoryRepository
            .findById(jobHistory.getId())
            .map(existingJobHistory -> {
                if (jobHistory.getStartDate() != null) {
                    existingJobHistory.setStartDate(jobHistory.getStartDate());
                }
                if (jobHistory.getEndDate() != null) {
                    existingJobHistory.setEndDate(jobHistory.getEndDate());
                }
                if (jobHistory.getLanguage() != null) {
                    existingJobHistory.setLanguage(jobHistory.getLanguage());
                }

                return existingJobHistory;
            })
            .map(jobHistoryRepository::save)
            .map(savedJobHistory -> {
                jobHistorySearchRepository.save(savedJobHistory);

                return savedJobHistory;
            });
    }

    @Override
    public Page<JobHistory> findAll(Pageable pageable) {
        log.debug("Request to get all JobHistories");
        return jobHistoryRepository.findAll(pageable);
    }

    @Override
    public Optional<JobHistory> findOne(String id) {
        log.debug("Request to get JobHistory : {}", id);
        return jobHistoryRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete JobHistory : {}", id);
        jobHistoryRepository.deleteById(id);
        jobHistorySearchRepository.deleteById(id);
    }

    @Override
    public Page<JobHistory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JobHistories for query {}", query);
        return jobHistorySearchRepository.search(query, pageable);
    }
}
