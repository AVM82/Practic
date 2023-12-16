import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CertificateInfo } from 'src/app/models/certificateInfo';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { CertificateService } from 'src/app/services/certificate.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-certificate-data',
  standalone: true,
  templateUrl: './certificate-data.component.html',
  styleUrls: ['./certificate-data.component.css'],
  imports: [FormsModule, CommonModule, MatDialogModule, MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatInputModule],
})
export class CertificateDataComponent implements OnInit {

  certificateInfo!: CertificateInfo;
  constructor(private tokenStorageService: TokenStorageService, public certificateService: CertificateService) { }
  
  ngOnInit(): void {
    this.certificateService.getCertificateInfo().subscribe( data => this.certificateInfo = data)
  }

  sendMessage() {
    console.log(this.certificateInfo.studentName);
    console.log(this.certificateInfo.courseName);
    console.log(this.certificateInfo.skills);
  };

  // onChange(skill: string, event: any) {
  //   var skillIndex = this.certificateInfo.skills.indexOf(skill);
  //   if (event.target.checked && skillIndex == -1) {
  //     this.certificateInfo.skills.push(skill);
  //     console.log(skill + "skill is enabled")
  //   } else if (!event.target.checked && skillIndex >= 0) {
  //     this.certificateInfo.skills.splice(skillIndex, 1);
  //     console.log(skill + "skill is disabled")
  //   }
  // }
}
