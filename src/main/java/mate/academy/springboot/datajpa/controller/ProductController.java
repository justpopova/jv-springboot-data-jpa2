package mate.academy.springboot.datajpa.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.springboot.datajpa.dto.request.ProductRequestDto;
import mate.academy.springboot.datajpa.dto.response.ProductResponseDto;
import mate.academy.springboot.datajpa.mapper.ProductMapper;
import mate.academy.springboot.datajpa.model.Product;
import mate.academy.springboot.datajpa.service.CategoryService;
import mate.academy.springboot.datajpa.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    public ProductController(ProductService productService,
                             ProductMapper productMapper, CategoryService categoryService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ProductResponseDto create(@RequestBody ProductRequestDto requestDto) {
        Product product = productService.save(
                productMapper.mapToModel(requestDto));
        return productMapper.mapToDto(product);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return productMapper.mapToDto(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable Long id,
                                     @RequestParam ProductRequestDto requestDto) {
        productService.update(id, requestDto.getTitle(), requestDto.getPrice());
        return productMapper.mapToDto(productService.getById(id));
    }

    @GetMapping
    public List<ProductResponseDto> getAllPriceBetween(@RequestParam BigDecimal from,
                                                       @RequestParam BigDecimal to) {
        List<Product> products = productService.getByPriceBetween(from, to);
        return products.stream()
                .map(productMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-category")
    public List<ProductResponseDto> getAllWithCategoryIdIn(
            @RequestParam List<Long> categoriesId) {
        return productService.getAllWithCategoryIdIn(categoriesId).stream()
                .map(productMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
