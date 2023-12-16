import { Component } from '@angular/core';
import { CertificateService } from 'src/app/services/certificate.service';
import { MatDialog } from '@angular/material/dialog';
import { CertificateDataComponent } from './certificate-data/certificate-data.component';

@Component({
  selector: 'app-certificate-request',
  standalone: true,
  templateUrl: './certificate-request.component.html',
  styleUrls: ['./certificate-request.component.css'],
})
export class CertificateRequestComponent {
     constructor(public dialog: MatDialog) { }

     certificateDialog() {
      this.dialog.open(CertificateDataComponent, {
        position: { top: '10px' },
        width: "50%",
      })
        console.log('kjsfadlkajflkdsjflsd')
     }
}
