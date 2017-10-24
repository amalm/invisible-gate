package com.skidata.facerec;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.server.PathParam;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;
import com.google.common.io.Files;

@RestController
public class FaceRecController {
	private final ConcurrentHashMap<String, File> imageMap = new ConcurrentHashMap<>();

	@GetMapping("/getVersion")
	public String getVersion() {
		String version = FaceRecController.class.getPackage().getImplementationVersion();
		return (Strings.isNullOrEmpty(version) ? "?" : version);
	}

	@PostMapping("/{userid}/addimage")
	public String addImage(@PathParam("userid") String userId, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {
		File imgFile;
		File tempDir = Files.createTempDir();
		imgFile = new File(tempDir, file.getOriginalFilename());
		try {
			file.transferTo(imgFile);
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException("Could not copy file content", e);
		}
		imageMap.put("1", imgFile);

		return "You successfully uploaded " + file.getOriginalFilename() + "!";
	}

	@GetMapping("/{userid}/getimage")
	@ResponseBody
	public ResponseEntity<Resource> getImage(@PathParam("userid") String userId) {
		if (!imageMap.contains("1")) 
			return ResponseEntity.notFound().build();
			
		Resource file = new FileSystemResource(imageMap.get("1"));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
