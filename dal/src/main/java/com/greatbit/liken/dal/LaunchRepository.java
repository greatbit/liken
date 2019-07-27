package com.greatbit.liken.dal;

import com.greatbit.liken.beans.Launch;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LaunchRepository extends LaunchRepositoryCustom,
        PagingAndSortingRepository<Launch, String> {
}
