package com.kwchina.wfm.interfaces;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kwchina.wfm.infrastructure.common.HttpHelper;

@Controller
public class FileUploadController {
	
	private static String FILE_UPLOAD_DIRECTORY = "/resources/";
	
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/formupload", method = RequestMethod.POST)
    public void handleFormUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            String fileName = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            if (file.getSize() > 0) {
                inputStream = file.getInputStream();
                System.out.println("size::" + file.getSize());

                fileName = context.getRealPath("") + FILE_UPLOAD_DIRECTORY + file.getOriginalFilename();
                outputStream = new FileOutputStream(fileName);
                System.out.println("fileName:" + file.getOriginalFilename());

                int readBytes = 0;
                byte[] buffer = new byte[10000];
                while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                        outputStream.write(buffer, 0, readBytes);
                }
                outputStream.close();
                inputStream.close();
            }

        } catch (Exception e) {
                e.printStackTrace();
        }
        
        HttpHelper.output(response, "done.");
    }

}
