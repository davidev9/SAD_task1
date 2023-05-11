package com.groom.manvsclass.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@GetMapping("/filterby/{text}/{category}")
	public	List<ClassUT>	searchAndFilter(@PathVariable String text,@PathVariable String category)
	{
		return srepo.searchAndFilter(text,category);
	}
	
	@GetMapping("filterby/{category}")
	public List<ClassUT> filterby(@PathVariable String category)
	{
		return srepo.filterByCategory(category);
	}
	
	@PostMapping("/insert")
	public ClassUT UploadClasse(@RequestBody ClassUT classe)
	{
		return repo.save(classe);
	}
	
	@PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile,@RequestParam("classe") ClassUT classe) throws IOException
    {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        FileUploadUtil.saveFile(fileName, multipartFile);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile");

        classe.setUri("/downloadFile"+fileName);
        classe.setDate(today.toString());
        repo.save(classe);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException
    {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        FileUploadUtil.saveFile(fileName, multipartFile);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
	
}
