package com.icici.dma.slabServiceImpl.ccrValuation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.slabDto.ccrValuation.ValuationMasterDTO;
import com.icici.dma.slabEntity.ccrValuation.ValuationMaster;
import com.icici.dma.slabRepository.ccrValuation.ValuationMasterRepository;
import com.icici.dma.slabService.ccrValuation.ValuationMasterService;

@Service
@Transactional
public class ValuationMasterServiceImpl
        implements ValuationMasterService {

    @Autowired
    private ValuationMasterRepository repository;

    @Override
    public ValuationMasterDTO create(
            ValuationMasterDTO request,
            String userId) {

        validateRequest(request);

        boolean exists =
                repository.existsByIboxIdAndValuerNameAndProductSubType(
                        request.getIboxId().trim(),
                        request.getValuerName().trim(),
                        request.getProductSubType().trim()
                );

		if (exists) {
			throw new RuntimeException("Valuation master record already exists");
		}

		ValuationMaster master = new ValuationMaster();

		master.setIboxId(request.getIboxId().trim());
		master.setValuerName(request.getValuerName().trim());
		master.setProductSubType(request.getProductSubType().trim());
		master.setStatus("APPROVED");

		master.setCreatedBy(userId);
		master.setCreatedDate(LocalDateTime.now());

		ValuationMaster saved = repository.save(master);

		return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ValuationMasterDTO> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ValuationMasterDTO> getActive() {

        return repository
                .findByStatusOrderById("APPROVED")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ValuationMasterDTO getById(Long id) {

        ValuationMaster master =
                repository.findById(id)
                    .orElseThrow(
                        () -> new RuntimeException(
                            "Valuation master record not found"
                        )
                    );

        return toDTO(master);
    }


    @Override
    public ValuationMasterDTO update(
            Long id,
            ValuationMasterDTO request,
            String userId) {

        validateRequest(request);

        ValuationMaster master =
                repository.findById(id)
                    .orElseThrow(
                        () -> new RuntimeException(
                            "Valuation master record not found"
                        )
                    );


        boolean exists =
                repository
                    .existsByIboxIdAndValuerNameAndProductSubTypeAndIdNot(
                        request.getIboxId().trim(),
                        request.getValuerName().trim(),
                        request.getProductSubType().trim(),
                        id
                    );

        if (exists) {
            throw new RuntimeException(
                    "Another Valuation master record already exists"
            );
        }

		master.setIboxId(request.getIboxId().trim());
		master.setValuerName(request.getValuerName().trim());
		master.setProductSubType(request.getProductSubType().trim());
		master.setModifiedBy(userId);
		master.setModifiedDate(LocalDateTime.now());

		ValuationMaster updated = repository.save(master);

		return toDTO(updated);
    }

    private ValuationMasterDTO toDTO(ValuationMaster master) {

        ValuationMasterDTO dto = new ValuationMasterDTO();

        dto.setId(master.getId());
        dto.setIboxId(master.getIboxId());
        dto.setValuerName(master.getValuerName());
        dto.setProductSubType(master.getProductSubType());

        dto.setStatus(master.getStatus());
        dto.setCreatedBy(master.getCreatedBy());
        dto.setCreatedDate(master.getCreatedDate());
        dto.setModifiedBy(master.getModifiedBy());
        dto.setModifiedDate(master.getModifiedDate());

        return dto;
    }

	private void validateRequest(ValuationMasterDTO request) {

		if (request == null) {
			throw new RuntimeException("Request cannot be null");
		}

		if (request.getIboxId() == null || request.getIboxId().trim().isEmpty()) {
			throw new RuntimeException("IBOX ID is required");
		}

		if (request.getValuerName() == null || request.getValuerName().trim().isEmpty()) {
			throw new RuntimeException("Valuer Name is required");
		}

		if (request.getProductSubType() == null || request.getProductSubType().trim().isEmpty()) {
			throw new RuntimeException("Product Sub Type is required");
		}
	}
}