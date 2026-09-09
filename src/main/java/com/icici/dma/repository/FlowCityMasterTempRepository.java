package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.model.FlowCityMasterTemp;

@Repository
public interface FlowCityMasterTempRepository extends JpaRepository<FlowCityMasterTemp, Long> {

    List<FlowCityMasterTemp> findByCreatedByAndStatus(String createdBy, String status);

    @Query(value = "SELECT * FROM TM_CCR_FLOW_CITY_MASTER_TEMP " +
            "WHERE STATUS = :status " +
            "AND CREATED_BY <> :user",
            nativeQuery = true)
    List<FlowCityMasterTemp> findAllByStatusAndCreatedByNot(
            @Param("status") String status,
            @Param("user") String user);

    boolean existsByCityCode(String cityCode);

    Optional<FlowCityMasterTemp> findByCityCode(String cityCode);

    boolean existsByCityCodeAndStatus(String cityCode, String status);

    Optional<FlowCityMasterTemp> findByCityCodeAndStatus(String cityCode, String status);


    @Query(value =
            "SELECT t.* " +
                    "FROM TM_CCR_FLOW_CITY_MASTER_TEMP t " +
                    "WHERE t.status = :status " +
                    "ORDER BY GREATEST( " +
                    "NVL(t.action_date, DATE '1900-01-01'), " +
                    "NVL(t.modified_date, DATE '1900-01-01'), " +
                    "NVL(t.created_date, DATE '1900-01-01') " +
                    ") DESC",
            nativeQuery = true)
    List<FlowCityMasterTemp> findAllByStatus(@Param("status") String status);

    @Query("SELECT f.cityCode FROM FlowCityMasterTemp f WHERE f.cityCode IN :codes")
    List<String> findExistingCityCodes(@Param("codes") List<String> codes);

    @Query("SELECT f.cityCode FROM FlowCityMasterTemp f WHERE f.cityCode IN :codes AND f.status = :status")
    List<String> findPendingIds(@Param("codes") List<String> codes,
                                @Param("status") String status);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
            "UPDATE TM_CCR_FLOW_CITY_MASTER_TEMP " +
                    "SET STATUS = :status, " +
                    "REMARKS = :remark, " +
                    "ACTION_DATE = SYSDATE " +
                    "WHERE CITY_CODE IN (:ids) " +
                    "AND STATUS = :pendingStatus",
            nativeQuery = true)
    int bulkUpdateStatus(@Param("ids") List<String> ids,
                         @Param("status") String status,
                         @Param("remark") String remark,
                         @Param("pendingStatus") String pendingStatus);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM FlowCityMasterTemp f WHERE f.cityCode IN :ids")
    int deleteApprovedRecords(@Param("ids") List<String> ids);

    List<FlowCityMasterTemp> findByCityCodeInAndStatus(
            List<String> cityCodes,
            String status);
}