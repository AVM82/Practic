import {CommonModule} from '@angular/common';
import {Component, Inject, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatInputModule} from '@angular/material/input';
import {CertificateInfo} from 'src/app/models/certificateInfo';
import {TokenStorageService} from 'src/app/services/token-storage.service';
import {CertificateService} from 'src/app/services/certificate.service';
import {CoursesService} from 'src/app/services/courses.service';

@Component({
    selector: 'app-certificate-data',
    standalone: true,
    templateUrl: './certificate-data.component.html',
    styleUrls: ['./certificate-data.component.css'],
    imports: [
        FormsModule,
        CommonModule,
        MatDialogModule,
        MatButtonModule,
        MatCheckboxModule,
        MatInputModule,
    ],
})
export class CertificateDataComponent implements OnInit {
    certificateInfo!: CertificateInfo;

    constructor(
        public tokenStorageService: TokenStorageService,
        public certificateService: CertificateService,
        public courseService: CoursesService,
        @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.certificateService.getCertificateInfo(this.data.student.id)
            .subscribe(cert => {
                this.certificateInfo = new CertificateInfo(
                    cert.studentName, cert.courseName, cert.skills, cert.start, cert.finish, cert.daysSpent);
            });
    }

    sendMessage() {
        console.log(this.certificateInfo)
        this.certificateInfo.skills = this.certificateInfo.selectedSkills
            .filter(s => s.enabledSkill).map(s => s.name);
        this.certificateService.postCertificateInfo(this.certificateInfo, this.data.student.id);
    };

    checkName(name: string): boolean {
        return /([А-ЯA-Z][- а-яА-Яa-zA-Z])/.exec(name) != null;
    }
}
