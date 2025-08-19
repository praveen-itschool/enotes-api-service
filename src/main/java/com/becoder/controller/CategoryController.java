package com.becoder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.service.CategoryService;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCategory(@RequestBody CategoryDto categoryDto) {
        boolean isSaved = categoryService.saveCategory(categoryDto);
        if (isSaved) {
            return new ResponseEntity<>("Category saved successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to save category", HttpStatus.BAD_REQUEST);
        }
    }

    // Get All Categories
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
    //	String nm=null;
    //	nm.toUpperCase();
        List<CategoryDto> categories = categoryService.getAllCategory();

        if (CollectionUtils.isEmpty(categories)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No categories found");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/active-category")
	public ResponseEntity<?> getActiveCategory() {

		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();//"Request successfully complete हो गया, लेकिन वापस कुछ data नहीं भेज रहे। सिर्फ 204 status code देंगे।"
		} else {
		     return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}
	}
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) {
        try {
            CategoryDto categoryDto = categoryService.getCategoryById(id);

            if (ObjectUtils.isEmpty(categoryDto)) {
                return new ResponseEntity<>("Category not found with id: " + id, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // custom exception (e.g., from service)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // for unexpected errors
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable  Integer id) {
		Boolean deleted = categoryService.deleteCategory(id);
		if (deleted) {
			 return new ResponseEntity<>("Category deleted success", HttpStatus.OK);
		}
		 return new ResponseEntity<>("Category Not deleted"+id, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
