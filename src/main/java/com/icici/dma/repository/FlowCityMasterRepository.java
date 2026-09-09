package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.FlowCityMaster;

@Repository
public interface FlowCityMasterRepository extends JpaRepository<FlowCityMaster, Long> {

    /**
     * Check whether City Code already exists
     */
    boolean existsByCityCode(String cityCode);

    /**
     * Find record by City Code
     */
    Optional<FlowCityMaster> findByCityCode(String cityCode);

    @Query(
            value =
                    "SELECT t.* " +
                            "FROM TM_CCR_FLOW_CITY_MASTER t " +
                            "WHERE t.status = ?1 " +
                            "ORDER BY GREATEST( " +
                            "NVL(t.modified_date, DATE '1900-01-01'), " +
                            "NVL(t.created_date, DATE '1900-01-01') " +
                            ") DESC",
            nativeQuery = true
    )
    List<FlowCityMaster> findAllByStatus(String status);

    List<FlowCityMaster> findByCityCodeIn(List<String> cityCodes);
}