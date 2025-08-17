package com.becoder.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;

@Service   // ✅ Service annotation सही है
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;   // ✅ Repository inject

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        category.setIsDeleted(false);   // नया category active रहेगा
        category.setCreatedBy(1);       // अभी hardcoded, बाद में login userId लेना चाहिए
        category.setCreatedOn(new Date()); // ✅ Date assign किया (क्योंकि BaseModel में Date है)
        Category saved = categoryRepository.save(category);

        if (ObjectUtils.isEmpty(saved)) {
            return false;
        }
        return true;
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(cat -> {
                    CategoryDto dto = new CategoryDto();
                    BeanUtils.copyProperties(cat, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {
      return null;
    }

    @Override
    public Category getCategoryById(Integer id) throws Exception {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new Exception("Category not found with id " + id);
        }
    }

    @Override
    public Boolean deleteCategory(Integer id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            Category category = optional.get();
            category.setIsDeleted(true); // Soft delete
            categoryRepository.save(category);
            return true;
        }
        return false;
    }
}
