package com.akshay.ecommerce.product.service;

import com.akshay.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.akshay.ecommerce.common.exception.ResourceNotFoundException;
import com.akshay.ecommerce.product.dto.ProductRequest;
import com.akshay.ecommerce.product.dto.ProductResponse;
import com.akshay.ecommerce.product.entity.Product;
import com.akshay.ecommerce.product.entity.ProductStatus;
import com.akshay.ecommerce.product.mapper.ProductMapper;
import com.akshay.ecommerce.product.repository.ProductRepository;
import com.akshay.ecommerce.product.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    @Override
    public ProductResponse saveProduct(ProductRequest productRequest) {

        Product product = productMapper.toEntity(productRequest);

        if(productRepository.existsBySku(productRequest.getSku()))
        {
            throw new ResourceAlreadyExistsException("Product already exists! : " + productRequest.getSku());
        }

        Product saved = productRepository.save(product);

        return productMapper.toProductResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable).map(product-> productMapper.toProductResponse(product));
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not found : " + id));

        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest, Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not found : " + id));

        if(!product.getSku().equals(productRequest.getSku())
                && productRepository.existsBySku(productRequest.getSku())) {
            throw new ResourceAlreadyExistsException("Product with SKU already exists: " + productRequest.getSku());

        }

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStatus(productRequest.getStatus());
        product.setSku(productRequest.getSku());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setBrand(productRequest.getBrand());

        Product updatedProduct = productRepository.save(product);

        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found : " + id));

        productRepository.delete(product);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProduct(String keyword, Pageable pageable) {

        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(keyword, keyword, pageable);

        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> filterByBrand(String brand, Pageable pageable) {

        Page<ProductResponse> productResponses = productRepository.findByBrandIgnoreCase(brand, pageable).map(product -> productMapper.toProductResponse(product));

        return productResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> filterByStatus(ProductStatus status, Pageable pageable) {

        Page<ProductResponse> productResponses = productRepository.findByStatus(status, pageable).map(product -> productMapper.toProductResponse(product));

        return productResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword, String brand, ProductStatus status, Pageable pageable) {

        Specification<Product> specification = null;

        if(keyword != null && !keyword.isBlank()) {
            specification = ProductSpecification.hasKeyword(keyword);
        }

        if(brand != null && !brand.isBlank()) {

            specification = specification == null
                    ? ProductSpecification.hasBrand(brand)
                    : specification.and(ProductSpecification.hasBrand(brand));
        }

        if(status !=null) {

          specification = specification == null
                  ? ProductSpecification.hasStatus(status)
                  : specification.and(ProductSpecification.hasStatus(status));
        }

        Page<ProductResponse> products = productRepository.findAll(specification, pageable).map(product -> productMapper.toProductResponse(product));

        return products;
    }
}
