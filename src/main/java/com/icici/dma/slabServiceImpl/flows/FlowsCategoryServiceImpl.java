package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsCategoryDTO;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;
import com.icici.dma.slabRepository.flows.FlowsCategoryRepository;
import com.icici.dma.slabService.flows.FlowsCategoryService;

@Service
@Transactional
public class FlowsCategoryServiceImpl implements FlowsCategoryService {

    @Autowired
    private FlowsCategoryRepository repo;

    @Override
	public String save(FlowsCategoryDTO dto, String user) {

		FlowsCategoryMaster entity = new FlowsCategoryMaster();

        entity.setCategoryName(dto.getCategoryName());
        entity.setStatus("APPROVED");
        entity.setCreatedBy(user);
        entity.setCreatedDate(new Date());

        repo.save(entity);

        return "Saved Successfully";

    }

    @Override
	public String update(FlowsCategoryDTO dto, String user) {

        FlowsCategoryMaster entity =
                repo.findById(dto.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category Not Found"));

        entity.setCategoryName(dto.getCategoryName());
        entity.setModifiedBy(user);
        entity.setModifiedDate(new Date());

        repo.save(entity);

        return "Updated Successfully";

    }

    @Override
    public List<FlowsCategoryMaster> getAll() {
        return repo.findAll();

    }

    @Override
    public List<FlowsCategoryMaster> getApproved() {

        return repo.findByStatusOrderByCategoryName(
                "APPROVED");

    }

}
