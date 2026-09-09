package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsBucketTypeDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketTypeMaster;
import com.icici.dma.slabRepository.flows.FlowsBucketTypeRepository;
import com.icici.dma.slabService.flows.FlowsBucketTypeService;

@Service
@Transactional
public class FlowsBucketTypeServiceImpl
        implements FlowsBucketTypeService {

    @Autowired
    private FlowsBucketTypeRepository repo;

    @Override
    public String save(
            FlowsBucketTypeDTO dto,
            String user) {

		if (dto.getBucketTypeName() == null || dto.getBucketTypeName().trim().isEmpty()) {

			throw new RuntimeException("Bucket Type Name is required");
		}

		String bucketTypeName = dto.getBucketTypeName().trim();

        // Duplicate check
		if (repo.findByBucketTypeName(bucketTypeName).isPresent()) {

			throw new RuntimeException("Bucket Type already exists");
		}

		FlowsBucketTypeMaster entity = new FlowsBucketTypeMaster();

        entity.setBucketTypeName(bucketTypeName);
        entity.setStatus("APPROVED");
        entity.setCreatedBy(user);
        entity.setCreatedDate(new Date());

        repo.save(entity);

        return "Bucket Type Saved Successfully";
    }


    @Override
    public String update(
            FlowsBucketTypeDTO dto,
            String user) {

		if (dto.getId() == null) {

			throw new RuntimeException("Bucket Type Id is required");
		}

        if (dto.getBucketTypeName() == null
                || dto.getBucketTypeName().trim().isEmpty()) {

			throw new RuntimeException("Bucket Type Name is required");
		}

        FlowsBucketTypeMaster entity =
                repo.findById(dto.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bucket Type Not Found"));

		String bucketTypeName = dto.getBucketTypeName().trim();

        // Duplicate check excluding current record
        Optional<FlowsBucketTypeMaster> existing =
                repo.findByBucketTypeNameAndIdNot(
                        bucketTypeName,
                        dto.getId());

        if (existing.isPresent()) {

			throw new RuntimeException("Bucket Type already exists");
		}

        entity.setBucketTypeName(bucketTypeName);
        entity.setModifiedBy(user);
        entity.setModifiedDate(new Date());

        repo.save(entity);

        return "Bucket Type Updated Successfully";
    }


    @Override
    public List<FlowsBucketTypeMaster> getAll() {

        return repo.findAll();
    }

    @Override
    public List<FlowsBucketTypeMaster> getApproved() {

        return repo.findByStatusOrderById("APPROVED");
    }
}