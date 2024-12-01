package com.project.shopapp.repository;

import com.project.shopapp.entity.TemporaryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryOrderRepository extends JpaRepository<TemporaryOrder, Long> {
}
