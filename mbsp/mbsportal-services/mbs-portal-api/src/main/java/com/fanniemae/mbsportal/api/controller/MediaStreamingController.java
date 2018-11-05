/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.api.controller;

import java.io.File;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date: Mar 23, 2018
 * @File: com.fanniemae.mbsportal.api.controller.MediaStreamingController.java
 * @Revision :
 * @Description: MediaStreamingController.java
 */
@RestController
public class MediaStreamingController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Media Streaming Controller Started");
    }

    private static final String EXTERNAL_AUDIO_DIRECTORY = "audio";

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaStreamingController.class);

    @RequestMapping(value = "/media/audio/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Void> downloadAudioMedia(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("fileName") String fileName) {
        LOGGER.debug("Entering downloadAudioMedia in MediaStreamingController");

        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            LOGGER.debug("Requestd File: {}", fileName);
            File file = new File(classLoader.getResource(EXTERNAL_AUDIO_DIRECTORY + "/" + fileName).getFile());
            InputStream inputStream = classLoader.getResourceAsStream(EXTERNAL_AUDIO_DIRECTORY + "/" + fileName);
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, response.getOutputStream());

        } catch (Exception ex) {
            LOGGER.error("Exception:", ex);
            LOGGER.error("Exiting downloadAudioMedia in MediaStreamingController");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        LOGGER.debug("Exiting downloadAudioMedia in MediaStreamingController");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
