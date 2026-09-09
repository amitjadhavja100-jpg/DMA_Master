package com.icici.dma.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.ProductDto;
import com.icici.dma.model.Product;
import com.icici.dma.repository.ProductRepository;
import com.icici.dma.service.ProductService;

@Service
public class ProductServiceImpl
        implements ProductService {

	@Autowired
    private ProductRepository productRepo;
	
	@Override
    public List<String> getProducts() {
        return productRepo.getProducts();
    }

    @Override
    public List<String> getSubProducts(String product) {
        return productRepo.getSubProducts(product);
    }

    @Override
	public List<String> getMasters(String product, String subProduct) {
		return productRepo.getMasters(product, subProduct);
    }
    
    @Override
    public void createProduct(ProductDto dto, String username) {

        // Product is mandatory
        if(dto.getProductName() == null ||
           dto.getProductName().trim().isEmpty()){

            throw new IllegalArgumentException("Product Name is mandatory");
        }

        // Validation
        if(dto.getMasterName() != null &&
           !dto.getMasterName().trim().isEmpty()){

            if(dto.getSubProduct() == null ||
               dto.getSubProduct().trim().isEmpty()){

                throw new IllegalArgumentException(
                    "Sub Product is mandatory for Master");
            }
        }

        Long count = productRepo.countProduct(
                dto.getProductName(),
                dto.getSubProduct(),
                dto.getMasterName());

        if(count > 0){

            throw new IllegalArgumentException("Record already exists");
        }

        Product product = new Product();

        product.setProductName(dto.getProductName());
        product.setSubProduct(dto.getSubProduct());
        product.setMasterName(dto.getMasterName());

        product.setCreatedBy(username);
        product.setCreatedDate(new Date());

        productRepo.save(product);
    }
}