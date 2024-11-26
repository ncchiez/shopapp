package com.project.shopapp.service;

import com.project.shopapp.dto.BrandDTO;
import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.entity.Brand;
import com.project.shopapp.entity.Category;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService{

    private final BrandRepository brandRepository;


    public Brand createBrand(BrandDTO brandDTO) {
        if(brandRepository.existsByName(brandDTO.getName())){
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .build();
        return brandRepository.save(brand);
    }


    public Brand getBrandById(long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(()->new AppException(ErrorCode.BRAND_NOT_EXISTED));
    }


    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }


    public Brand updateBrand(long brandId, BrandDTO brandDTO) {
        Brand existingBrand = getBrandById(brandId);
        existingBrand.setName(brandDTO.getName());
        return brandRepository.save(existingBrand);
    }


    public void deleteBrand(long brandId) {
        Brand existingBrand = getBrandById(brandId);
        brandRepository.deleteById(brandId);
    }
}
