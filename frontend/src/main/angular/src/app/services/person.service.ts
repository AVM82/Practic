import { Injectable } from "@angular/core";
import { TokenStorageService } from "./token-storage.service";
import { User } from "src/app/models/user";
import { HttpClient } from "@angular/common/http";
import { ApiUrls, addRoleUrl, getApplicationCheckUrl, getApplicationUrl, removeRoleUrl } from "src/app/enums/api-urls";
import { Applicant } from "src/app/models/applicant";
import { InfoMessagesService } from "./info-messages.service";
import { Observable } from "rxjs/internal/Observable";

@Injectable({
    providedIn: 'root'
  })
export class PersonService {
    me: User;

    constructor(
        private http: HttpClient,
        private tokenStorageService: TokenStorageService,
        private messagesService: InfoMessagesService
    ) {
        this.me = this.tokenStorageService.me!;
    }

    getAllUsers(): Observable<User[]> {
        return this.http.get<User[]>(ApiUrls.Persons);
    }

    createApplication(slug: string): void {
        if (confirm("Ви дійсно хочете записатися на цей курс?"))
            this.http.post<Applicant>(getApplicationUrl(slug), {}).subscribe({
                next: applicant => {
                    this.me.addAplicant({applicantId: applicant.id, slug: applicant.slug});
                    this.tokenStorageService.saveUser(this.me);
                    this.messagesService.showMessage("Заявка прийнята", "normal");
                },
                error: error => { 
                    console.error('Помилка при відправці заявки', error);
                    this.messagesService.showMessage("Помилка відправки заявки", "error")
                }
            });
    }

    checkApplicant(id: number): void {
        this.http.get<Applicant>(getApplicationCheckUrl(id)).subscribe(applicant => {
            if (applicant.id == 0) 
                this.tokenStorageService.refreshMe();
            else
                if (applicant.isApplied) {
                    this.me.removeApplicantById(id);
                    this.me.students!.push(applicant.student);
                    this.tokenStorageService.saveUser(this.me);
                }
        });
    }

    addRole(user: User, role: string): void {
        this.http.post<User>(addRoleUrl(user.id, role), {}).subscribe(updated => {
            user.update(updated);
            if (updated.id === this.me.id) 
                this.tokenStorageService.updateMe(user);
        })
    }

    removeRole(user: User, role: string): void {
        this.http.post<User>(removeRoleUrl(user.id, role), {}).subscribe(updated => {
            user.update(updated);
            if (updated.id === this.me.id)
            this.tokenStorageService.updateMe(user);
        })
    }

}