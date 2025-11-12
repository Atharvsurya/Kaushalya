package com.kaushalya.web.Repository;

import com.kaushalya.web.Entity.MenteeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeRepository extends JpaRepository<MenteeEntity,Long> {
}
