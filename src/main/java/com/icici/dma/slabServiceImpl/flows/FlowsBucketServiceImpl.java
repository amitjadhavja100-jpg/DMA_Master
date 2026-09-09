package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsBucketDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;
import com.icici.dma.slabRepository.flows.FlowsBucketRepository;
import com.icici.dma.slabRepository.flows.FlowsBucketTypeRepository;
import com.icici.dma.slabRepository.flows.FlowsCategoryRepository;
import com.icici.dma.slabService.flows.FlowsBucketService;

@Service
@Transactional
public class FlowsBucketServiceImpl implements FlowsBucketService {

    @Autowired
    private FlowsBucketRepository repo;
    
    @Autowired
    private FlowsBucketTypeRepository bucketTypeRepo;
    
    @Autowired
    private FlowsCategoryRepository categoryRepo;
    
    @Override
    public String save(FlowsBucketDTO dto, String user) {

        if (dto.getCategoryId() == null) {
            throw new RuntimeException("Category is required");
        }

        if (dto.getBucketTypeId() == null) {
            throw new RuntimeException("Bucket Type is required");
        }

        if (dto.getOrderId() == null) {
            throw new RuntimeException("Order Id is required");
        }

        if (dto.getBucketName() == null ||
                dto.getBucketName().trim().isEmpty()) {

            throw new RuntimeException("Bucket Name is required");
        }

        // CFP
        boolean isCFP = isCFPCategory(dto.getCategoryId());

        if (isCFP) {

            // City MUST be null for CFP
            dto.setCityId(null);

            if (repo.findByCategoryIdAndBucketTypeIdAndBucketName(
                    dto.getCategoryId(),
                    dto.getBucketTypeId(),
                    dto.getBucketName()).isPresent()) {

                throw new RuntimeException(
                        "Bucket already exists for this category and bucket type");
            }

            if (repo.findByCategoryIdAndBucketTypeIdAndOrderId(
                    dto.getCategoryId(),
                    dto.getBucketTypeId(),
                    dto.getOrderId()).isPresent()) {

                throw new RuntimeException(
                        "Order Id already exists for this category and bucket type");
            }

        }

        // CATEGORY like A
        else {

            if (dto.getCityId() == null) {
                throw new RuntimeException("City is required");
            }

            if (repo.findByCityIdAndBucketTypeIdAndBucketName(
                    dto.getCityId(),
                    dto.getBucketTypeId(),
                    dto.getBucketName()).isPresent()) {

                throw new RuntimeException(
                        "Bucket already exists for this city and bucket type");
            }

            if (repo.findByCityIdAndBucketTypeIdAndOrderId(
                    dto.getCityId(),
                    dto.getBucketTypeId(),
                    dto.getOrderId()).isPresent()) {

                throw new RuntimeException(
                        "Order Id already exists for this city and bucket type");
            }
        }

        bucketTypeRepo.findById(dto.getBucketTypeId())
                .orElseThrow(() ->
                        new RuntimeException("Bucket Type Not Found"));

        FlowsBucketMaster entity = new FlowsBucketMaster();

        entity.setCategoryId(dto.getCategoryId());
        entity.setCityId(dto.getCityId());
        entity.setBucketTypeId(dto.getBucketTypeId());
        entity.setBucketName(dto.getBucketName().trim());
        entity.setTableType(dto.getTableType());
        entity.setOrderId(dto.getOrderId());
        entity.setRangeRequired(dto.getRangeRequired());

        entity.setStatus("APPROVED");
        entity.setCreatedBy(user);
        entity.setCreatedDate(new Date());

        repo.save(entity);

        return "Saved Successfully";
    }

//    @Override
//	public String update(FlowsBucketDTO dto, String user) {
//
//        FlowsBucketMaster entity = repo.findById(dto.getId())
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Bucket Not Found"));
//        
//		if (dto.getCityId() == null) {
//			throw new RuntimeException("City is required");
//		}
//
//		if (dto.getBucketTypeId() == null) {
//	        throw new RuntimeException("Bucket Type is required");
//	    }
//
//	    if (dto.getOrderId() == null) {
//	        throw new RuntimeException("Order Id is required");
//	    }
//	    
//		if (dto.getBucketName() == null || dto.getBucketName().trim().isEmpty()) {
//
//			throw new RuntimeException("Bucket Name is required");
//		}
//
//	    bucketTypeRepo.findById(dto.getBucketTypeId())
//	        .orElseThrow(() ->
//	            new RuntimeException("Bucket Type Not Found"));
//
//        
//        // Duplicate bucket name
//	    Optional<FlowsBucketMaster> existingBucket =
//	            repo.findByCityIdAndBucketTypeIdAndBucketNameAndIdNot(
//	                    dto.getCityId(),
//	                    dto.getBucketTypeId(),
//	                    dto.getBucketName(),
//	                    dto.getId());
//
//	    if (existingBucket.isPresent()) {
//
//	        throw new RuntimeException(
//	            "Bucket already exists for this city and bucket type");
//	    }
//        
//		// Duplicate order ID
//		Optional<FlowsBucketMaster> existingOrder =
//		        repo.findByCityIdAndBucketTypeIdAndOrderIdAndIdNot(
//		                dto.getCityId(),
//		                dto.getBucketTypeId(),
//		                dto.getOrderId(),
//		                dto.getId());
//
//		if (existingOrder.isPresent()) {
//
//			throw new RuntimeException("Order Id already exists for this city and bucket type");
//		}
//		
//		entity.setBucketTypeId(dto.getBucketTypeId());
//        entity.setBucketName(dto.getBucketName());
//        entity.setTableType(dto.getTableType());
//        entity.setOrderId(dto.getOrderId());
//        entity.setRangeRequired(dto.getRangeRequired());
//        
//        entity.setModifiedBy(user);
//        entity.setModifiedDate(new Date());
//
//        repo.save(entity);
//
//        return "Updated Successfully";
//
//    }
    
    @Override
    public String update(FlowsBucketDTO dto, String user) {

        FlowsBucketMaster entity = repo.findById(dto.getId())
                .orElseThrow(() ->
                        new RuntimeException("Bucket Not Found"));
        if (dto.getCategoryId() == null) {
            throw new RuntimeException("Category is required");
        }

        if (dto.getBucketTypeId() == null) {
            throw new RuntimeException("Bucket Type is required");
        }

        if (dto.getOrderId() == null) {
            throw new RuntimeException("Order Id is required");
        }

        if (dto.getBucketName() == null ||
                dto.getBucketName().trim().isEmpty()) {

            throw new RuntimeException("Bucket Name is required");
        }

        // Validate category
        FlowsCategoryMaster category =
                categoryRepo.findById(dto.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException("Category Not Found"));

        // Validate bucket type
        bucketTypeRepo.findById(dto.getBucketTypeId())
                .orElseThrow(() ->
                        new RuntimeException("Bucket Type Not Found"));

        boolean isCFP =
                "CFP".equalsIgnoreCase(
                        category.getCategoryName().trim()
                );

        // CFP CATEGORY
        if (isCFP) {

            // CFP does not belong to a city
            dto.setCityId(null);

            // Duplicate bucket name
            Optional<FlowsBucketMaster> existingBucket =
                    repo.findByCategoryIdAndBucketTypeIdAndBucketNameAndIdNot(
                            dto.getCategoryId(),
                            dto.getBucketTypeId(),
                            dto.getBucketName().trim(),
                            dto.getId());

            if (existingBucket.isPresent()) {

                throw new RuntimeException(
                        "Bucket already exists for this category and bucket type");
            }

            // Duplicate order ID
            Optional<FlowsBucketMaster> existingOrder =
                    repo.findByCategoryIdAndBucketTypeIdAndOrderIdAndIdNot(
                            dto.getCategoryId(),
                            dto.getBucketTypeId(),
                            dto.getOrderId(),
                            dto.getId());

            if (existingOrder.isPresent()) {

                throw new RuntimeException(
                        "Order Id already exists for this category and bucket type");
            }
        }

        // CITY BASED CATEGORY
        else {

            if (dto.getCityId() == null) {
                throw new RuntimeException("City is required");
            }

            // Duplicate bucket name
            Optional<FlowsBucketMaster> existingBucket =
                    repo.findByCityIdAndBucketTypeIdAndBucketNameAndIdNot(
                            dto.getCityId(),
                            dto.getBucketTypeId(),
                            dto.getBucketName().trim(),
                            dto.getId());

            if (existingBucket.isPresent()) {

                throw new RuntimeException(
                        "Bucket already exists for this city and bucket type");
            }

            // Duplicate order ID
            Optional<FlowsBucketMaster> existingOrder =
                    repo.findByCityIdAndBucketTypeIdAndOrderIdAndIdNot(
                            dto.getCityId(),
                            dto.getBucketTypeId(),
                            dto.getOrderId(),
                            dto.getId());

            if (existingOrder.isPresent()) {

                throw new RuntimeException(
                        "Order Id already exists for this city and bucket type");
            }
        }

        entity.setCategoryId(dto.getCategoryId());
        entity.setCityId(dto.getCityId());
        entity.setBucketTypeId(dto.getBucketTypeId());
        entity.setBucketName(dto.getBucketName().trim());
        entity.setTableType(dto.getTableType());
        entity.setOrderId(dto.getOrderId());
        entity.setRangeRequired(dto.getRangeRequired());

        entity.setModifiedBy(user);
        entity.setModifiedDate(new Date());

        repo.save(entity);

        return "Updated Successfully";
    }

    @Override
    public List<FlowsBucketMaster> getAll() {
        return repo.findAll();
    }

	@Override
	public List<FlowsBucketMaster> getApproved(Long cityId) {

		return repo.findByCityIdAndStatusOrderById(cityId, "APPROVED");
	}
	
	private boolean isCFPCategory(Long categoryId) {

	    FlowsCategoryMaster category =
	            categoryRepo.findById(categoryId)
	                    .orElseThrow(() ->
	                            new RuntimeException("Category Not Found"));

	    return "CFP".equalsIgnoreCase(
	            category.getCategoryName().trim()
	    );
	}

}