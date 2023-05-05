package com.groom.manvsclass.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.repository.ClassRepository;
import com.groom.manvsclass.repository.SearchRepository;
import com.groom.manvsclass.repository.SearchRepositoryImpl;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class PostController {
	
	@Autowired
	ClassRepository repo;
	
	
	private final SearchRepositoryImpl srepo;
	
	public PostController(SearchRepositoryImpl srepo)
	{
		this.srepo=srepo;
	}
	
	@GetMapping("/home")
	public	List<ClassUT>	getAllClasses()
	{
		return repo.findAll();
	}
	
	@GetMapping("/home/{text}")
	public	List<ClassUT>	search(@PathVariable String text)
	{
		return srepo.findByText(text);
	}
	
	
	@PostMapping("/insert")
	public ClassUT UploadClasse(@RequestBody ClassUT classe)
	{
		return repo.save(classe);
	}
	
}
