import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { TokenStorageService } from "src/app/services/auth/token-storage.service";

@Component({
    selector: 'app-edit-btn',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './edit-btn.component.html',
    styleUrls: ['./edit-btn.component.css']
  })
export class EditBtnComponent implements OnInit {
    editMode: boolean = false;
    slug: string = '';
  
    constructor(
        private tokenStorageService: TokenStorageService,
        private route: ActivatedRoute
    ) {}

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            this.slug = params.get('slug')!;
        })
    }

    changeMode(): void {
        this.editMode = !this.editMode;
    }

    showEditCapability(): Observable<boolean> {
        return this.tokenStorageService.isMentor(this.slug, true);        
    }
}