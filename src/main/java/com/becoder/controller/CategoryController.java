package com.becoder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
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
}
