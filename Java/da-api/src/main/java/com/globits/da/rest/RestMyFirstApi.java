package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.REST_POST_DTO;
import com.globits.da.service.MyFirtApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/myfirstapi")
public class RestMyFirstApi {
    @Autowired
    private MyFirtApiService myFirtApiService;

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/getString")
    public String getMyFirstApi() {
        return myFirtApiService.getMyFirtApi();
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @PostMapping("/postString")
    public REST_POST_DTO postString( REST_POST_DTO dto) {
        return dto;
    }
    @Secured({ AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @PostMapping("/postFile")
    public String postString(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            return "File processed successfully!";
        }
        return "No file uploaded!";
    }
}
