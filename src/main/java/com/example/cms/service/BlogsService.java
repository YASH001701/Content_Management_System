package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.dto.BlogRequest;
import com.example.cms.dto.BlogResponse;
import com.example.cms.util.Responstructure;

public interface BlogsService {

	ResponseEntity<Responstructure<BlogResponse>> createBlog( BlogRequest blog, int userId);

	ResponseEntity<Responstructure<Boolean>> checkBlogTitleAvailable(String title);

	ResponseEntity<Responstructure<BlogResponse>> findBlogById(int blogId);

	ResponseEntity<Responstructure<BlogResponse>> updateBlogdata(BlogRequest blogRequest, int blogId);

}
