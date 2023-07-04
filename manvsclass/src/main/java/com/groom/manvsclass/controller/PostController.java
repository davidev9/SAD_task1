package com.groom.manvsclass.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.groom.manvsclass.api.upload.FileUploadResponse;
import com.groom.manvsclass.api.upload.FileUploadUtil;
import com.groom.manvsclass.api.download.FileDownloadUtil;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.repository.ClassRepository;

import com.groom.manvsclass.repository.SearchRepositoryImpl;


import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
public class PostController {
	
	@Autowired
	ClassRepository repo;
	@Autowired
    private MongoTemplate mongoTemplate; 
	
	private final LocalDate today = LocalDate.now();
	private final SearchRepositoryImpl srepo;
	
	public PostController(SearchRepositoryImpl srepo)
	{
		this.srepo=srepo;
	}

	@GetMapping("/home")
	public	List<ClassUT>	elencaClassi()
	{
		return repo.findAll();
	}
	
	

	@GetMapping("filterby/{category}")
	public List<ClassUT> elencaClassi(@PathVariable String category)
	{
		return srepo.filterByCategory(category);
	}
	
	

	@GetMapping("/filterby/{text}/{category}")
	public	List<ClassUT>	elencaClassi(@PathVariable String text,@PathVariable String category)
	{
		return srepo.searchAndFilter(text,category);
	}

	@PostMapping("/insert")
	public ClassUT UploadClasse(@RequestBody ClassUT classe)
	{
		return repo.save(classe);
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile,@RequestParam("model") String model) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		ClassUT classe = mapper.readValue(model, ClassUT.class);
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		long size = multipartFile.getSize();
		
		FileUploadUtil.saveCLassFile(fileName,classe.getName() ,multipartFile);
		
		FileUploadResponse response = new FileUploadResponse();
		response.setFileName(fileName);
		response.setSize(size);
		response.setDownloadUri("/downloadFile");
		
		classe.setUri("Files-Upload/"+classe.getName()+"/"+fileName);
		classe.setDate(today.toString());
		repo.save(classe);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("delete/{name}")
	public ClassUT eliminaClasse(@PathVariable String name) {
		Query query= new Query(); 
	   query.addCriteria(Criteria.where("name").is(name));
	   this.eliminaFile(name);
	   return mongoTemplate.findAndRemove(query, ClassUT.class);
	}

	@PostMapping("/deleteFile/{fileName}")
	public ResponseEntity<String> eliminaFile(@PathVariable String fileName) {
	  String folderPath = "Files-Upload/"+ fileName; 
	  
	        File folderToDelete = new File(folderPath);
	        if (folderToDelete.exists() && folderToDelete.isDirectory()) {
	        	try {
	        		FileUploadUtil.deleteDirectory(folderToDelete);
	                return new ResponseEntity<>("Cartella eliminata con successo.", HttpStatus.OK);
	            } catch (IOException e) {
	                return new ResponseEntity<>("Impossibile eliminare la cartella.", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        } else {
	            return new ResponseEntity<>("Cartella non trovata.", HttpStatus.NOT_FOUND);
	        }
	 }
	    
	

	
	@GetMapping("/home/{text}")
	public	List<ClassUT>	ricercaClasse(@PathVariable String text)
	{
		return srepo.findByText(text);
	}
	
	
	@GetMapping("/downloadFile/{name}")
	    public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) throws Exception {
		 	   List<ClassUT> classe= srepo.findByText(name);
		 	   
	           return FileDownloadUtil.downloadClassFile(classe.get(0).getcode_Uri());
	    }
	 

@PostMapping("update/{name}")
public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent) {
			Query query= new Query();
			
		   query.addCriteria(Criteria.where("name").is(name));
		    Update update = new Update().set("name", newContent.getName())
	                .set("date", newContent.getDate())
	                .set("difficulty", newContent.getDifficulty())
	                .set("description", newContent.getDescription())
	                .set("category", newContent.getCategory());
		    long modifiedCount = mongoTemplate.updateFirst(query, update, ClassUT.class).getModifiedCount();

	        if (modifiedCount > 0) {
	            return new ResponseEntity<>("Aggiornamento eseguito correttamente.", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Nessuna classe trovata o nessuna modifica effettuata.", HttpStatus.NOT_FOUND);
	        }
	    } 

	/*
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
	}*/
	
}
