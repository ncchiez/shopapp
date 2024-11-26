package com.project.shopapp.controller;

import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.dto.UploadFileDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.ProductMapper;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.ProductListResponse;
import com.project.shopapp.response.ProductResponse;
import com.project.shopapp.service.CategoryService;
import com.project.shopapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping("")
    ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO){
        Product newProduct = productService.createProduct(productDTO);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(ProductResponse.fromProduct(newProduct))
                .build());
    }

    /**
     Create ảnh sau khi đã thêm product
    **/
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") long productId, @ModelAttribute UploadFileDTO uploadFileDTO) {
        try {

            Product existsProduct = productService.getProductById(productId);
            List<MultipartFile> files = uploadFileDTO.getFiles();
            files = (files == null) ? new ArrayList<>() : files;


            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                throw new AppException(ErrorCode.PRODUCT_IMAGE_INVALID);
            }

            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }

                // Kiểm tra kích thước và định dạng file
                if (file.getSize() > 10 * 1024 * 1024) { // kích thước lớn hơn 10MB
                    throw new AppException(ErrorCode.FILES_IMAGES_SIZE_FAILED);
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new AppException(ErrorCode.FILES_IMAGES_TYPE_FAILED);
                }

                // lưu file và cập nhật thumnail trong DTO
                String fileName = storeFile(file);

                // lưu vào đối tượng product trong DB ->
                ProductImage productImage = productService.createProductImage(
                        existsProduct.getId(),
                        ProductImageDTO.builder().imageUrl(fileName).build()
                );
                productImages.add(productImage);

            }
            productService.convertThumbnail(productImages.getFirst(), existsProduct);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .payload(productImages.stream().map(ProductImage::getImageUrl).collect(Collectors.toList()))
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage()).build());
        }
    }


    @GetMapping("")
    ResponseEntity<?> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit){

        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("createdAt").descending());

        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                        .build())
                .build());
    }
    /**
     Xem ảnh
     **/
    @GetMapping("/images/{image-name}")
    public ResponseEntity<?> viewImage(@PathVariable("image-name") String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());

            if (urlResource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri().toString()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     Chi tiết sản phẩm
     **/
    @GetMapping("/{id}")
    ResponseEntity<?> getProductDetailById(@PathVariable long id){

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(productService.getProductDetail(id))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct (@PathVariable long id, @RequestBody ProductDTO productDTO){
        Product product = productService.updateProduct(id,productDTO);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(ProductResponse.fromProduct(product))
                .build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProduct (@PathVariable long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Product deleted")
                .build());
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image file");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID() + "_" + fileName;
        // đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // kiểm tra và tạo thư mục nêú nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // sao chép file vào thư mục
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


}
