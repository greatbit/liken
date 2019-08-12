package com.testquack.liken.dal;

import com.testquack.liken.beans.Launch;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LaunchRepository extends LaunchRepositoryCustom,
        PagingAndSortingRepository<Launch, String> {
}
