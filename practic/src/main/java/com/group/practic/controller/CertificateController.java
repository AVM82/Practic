package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import com.group.practic.dto.CertificateDto;
import com.group.practic.service.CertificateService;
import com.group.practic.service.StudentService;
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
@PreAuthorize("hasRole('STUDENT')")
public class CertificateController {
    private final CertificateService certificateService;

    @GetMapping("/{studentId}")
    public ResponseEntity<CertificateDto> getCertificateInfo(@PathVariable Long studentId) {
        return getResponse(certificateService.getCertificateInfo(studentId));
    }

    @PostMapping("/request/{studentId}")
    public ResponseEntity<CertificateDto> sendCertificateRequest(
            @RequestBody CertificateDto requestDto, @PathVariable Long studentId) {
        return getResponse(certificateService.sendCertificateRequest(requestDto, studentId));
    }
}
