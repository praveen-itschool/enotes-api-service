package com.becoder.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;

@Service // ✅ Service annotation सही है
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository; // ✅ Repository inject
	@Autowired
	private ModelMapper mapper;

	
	
	/*
@Override
public Boolean saveCategory(CategoryDto categoryDto) {
    Category category = new Category();

    // DTO → Entity copy
    BeanUtils.copyProperties(categoryDto, category);

    // Extra fields manually set करने पड़ेंगे (क्योंकि DTO में ये नहीं होते)
    category.setIsDeleted(false); // नया category delete नहीं रहेगा
    category.setCreatedBy(1);     // अभी hardcoded, बाद में login userId लेना चाहिए
    category.setCreatedOn(new Date()); // BaseModel का createdOn field

    Category saved = categoryRepository.save(category);

    if (ObjectUtils.isEmpty(saved)) {
        return false;
    }
    return true;
}

	 */
	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
		/*
		 * Category category = new Category(); BeanUtils.copyProperties(categoryDto,
		 * category); category.setIsDeleted(false); // नया category active रहेगा
		 * category.setCreatedBy(1); // अभी hardcoded, बाद में login userId लेना चाहिए
		 * category.setCreatedOn(new Date()); // ✅ Date assign किया (क्योंकि BaseModel
		 * में Date है) Category saved = categoryRepository.save(category);
		 * 
		 * if (ObjectUtils.isEmpty(saved)) { return false; }
		 * 
		 */

		
		Category category = mapper.map(categoryDto, Category.class);
		//Category category = new Category();
		//category.setName(categoryDto.getName());
		//category.setDescription(categoryDto.getDescription());
	//	category.setIsActive(categoryDto.getIsActive());
		category.setIsDeleted(false); // नया category active रहेगा
		category.setCreatedBy(1); // अभी hardcoded, बाद में login userId लेना चाहिए
		category.setCreatedOn(new Date()); // ✅ Date assign किया (क्योंकि BaseModel में Date है)
		Category saved = categoryRepository.save(category);

		if (ObjectUtils.isEmpty(saved)) {
			return false;
		}
		return true;
	}
/*
	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(cat -> {
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(cat, dto);
			return dto;
		}).collect(Collectors.toList());
	}
*/
	
	@Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();

        return categories.stream()
                .map(cat -> mapper.map(cat, CategoryDto.class)) // ✅ BeanUtils की जगह
                .collect(Collectors.toList());
    }
	@Override
	public List<CategoryResponse> getActiveCategory() {
	    List<Category> categories = categoryRepository.findByisActiveTrue();

	    List<CategoryResponse> categoryResponse = categories.stream()
	            .map(cat -> mapper.map(cat, CategoryResponse.class)) // ✅ हर entity को map करें
	            .collect(Collectors.toList());

	    return categoryResponse;
	}

	/*
@Override
public List<CategoryResponse> getActiveCategory() {
    List<Category> categories = categoryRepository.findByIsActiveTrue();

    List<CategoryResponse> categoryResponses = categories.stream().map(cat -> {
        CategoryResponse dto = new CategoryResponse();
        BeanUtils.copyProperties(cat, dto);
        return dto;
    }).collect(Collectors.toList());

    return categoryResponses;
}

	 * 
	 */
	// stream api without stream api with beanutils.copy prorties
	/*
@Override
public List<CategoryResponse> getActiveCategory() {
    List<Category> categories = categoryRepository.findByIsActiveTrue();
    List<CategoryResponse> categoryResponses = new ArrayList<>();

    for (Category cat : categories) {
        CategoryResponse dto = new CategoryResponse();
        BeanUtils.copyProperties(cat, dto);
        categoryResponses.add(dto);
    }

    return categoryResponses;
}

	 */

	@Override
	public CategoryDto getCategoryById(Integer id) {
	    Optional<Category> optionalCategory = categoryRepository.findByIdAndIsDeletedFalse(id);

	    if (optionalCategory.isPresent()) {
	        Category category = optionalCategory.get();
	        category.setName(category.getName().toUpperCase());
	        category.setDescription(category.getDescription().toUpperCase());
	        return mapper.map(category, CategoryDto.class);
	    } else {
	       return null;
	    }
	}

	@Override
	//@CacheEvict(value = "getCategoryById" , key = "#id")
	public Boolean deleteCategory(Integer id) {
		Optional<Category> findByCatgeory = categoryRepository.findByIdAndIsDeletedFalse(id);

		if (findByCatgeory.isPresent()) {
			Category category = findByCatgeory.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			
			// remove from cache
		//	cacheService.removeCacheByName(Arrays.asList("allCategory","activeCategory"));
			
			return true;
		}
		return false;
	

}
}
