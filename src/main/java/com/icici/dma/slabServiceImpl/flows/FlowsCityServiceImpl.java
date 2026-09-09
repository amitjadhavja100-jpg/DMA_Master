package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsCityDTO;
import com.icici.dma.slabEntity.flows.FlowsCityMaster;
import com.icici.dma.slabRepository.flows.FlowsCityRepository;
import com.icici.dma.slabService.flows.FlowsCityService;

@Service
@Transactional
public class FlowsCityServiceImpl implements FlowsCityService {

    @Autowired
    private FlowsCityRepository repo;


    @Override
    public String save(FlowsCityDTO dto, String user) {

		if (dto.getCategoryId() == null) {
			throw new RuntimeException("Category is required");
		}

		if (dto.getCityName() == null || dto.getCityName().trim().isEmpty()) {

			throw new RuntimeException("City Name is required");
		}

		if (repo.findByCategoryIdAndCityName(dto.getCategoryId(), dto.getCityName().trim()).isPresent()) {
			
			throw new RuntimeException("City already exists for this category");
		}

		FlowsCityMaster entity = new FlowsCityMaster();

		entity.setCategoryId(dto.getCategoryId());
		entity.setCityName(dto.getCityName().trim());
		entity.setStatus("APPROVED");
		entity.setCreatedBy(user);
		entity.setCreatedDate(new Date());

		repo.save(entity);

		return "City Saved Successfully";
    }


	@Override
	public String update(FlowsCityDTO dto, String user) {

        FlowsCityMaster entity = repo.findById(dto.getId())
                .orElseThrow(() ->
                    new RuntimeException(
                        "City Not Found"
                    )
                );

		entity.setCategoryId(dto.getCategoryId());
		entity.setCityName(dto.getCityName().trim());
        entity.setModifiedBy(user);
        entity.setModifiedDate(new Date());

        repo.save(entity);

        return "City Updated Successfully";
    }


    @Override
    public List<FlowsCityMaster> getAll() {

        return repo.findAll();
    }


    @Override
    public List<FlowsCityMaster>
    getApprovedByCategory(Long categoryId) {

        return repo.findByCategoryIdAndStatusOrderByCityName(
                categoryId,
                "APPROVED"
        );
    }
}