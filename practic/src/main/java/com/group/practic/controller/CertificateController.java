package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.CertificateDto;
import com.group.practic.service.CertificateService;
import com.group.practic.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification")
public class CertificateController {
    private final CertificateService certificateService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CertificateDto> getCertificateInfo(@PathVariable Long studentId) {
        return getResponse(certificateService.getCertificateInfo(studentId));
    }

    @PostMapping
    public ResponseEntity<Object> sendCertificateRequest(@RequestBody CertificateDto requestDto) {
        emailSenderService.sendMessage(null);
        return ResponseEntity.ok().build();
    }
}
