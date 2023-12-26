import {Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {CertificateDataComponent} from './certificate-data/certificate-data.component';
import {StateStudent} from 'src/app/models/student';

@Component({
    selector: 'app-certificate-request',
    standalone: true,
    templateUrl: './certificate-request.component.html',
    styleUrls: ['./certificate-request.component.css']
})
export class CertificateRequestComponent {

    @Input() student!: StateStudent;

    constructor(public dialog: MatDialog) {
    }

    openDialog() {
        this.dialog.open(CertificateDataComponent, {
            width: "50%",
            data: {student: this.student}
        })
    }
}
