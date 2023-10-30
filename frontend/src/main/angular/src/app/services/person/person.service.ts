import { Injectable } from "@angular/core";
import { TokenStorageService } from "../auth/token-storage.service";
import { User } from "src/app/models/user/user";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/internal/Observable";
import { getApplicationCheckUrl, getApplicationUrl } from "src/app/enums/api-urls";
import { StateStudent } from "src/app/models/user/student";
import { Applicant } from "src/app/models/user/applicant";
import { httpOptions } from "src/app/constants";

@Injectable({
    providedIn: 'root'
  })
export class PersonService {
    me: User;

    constructor(
        private http: HttpClient,
        private tokenStorageService: TokenStorageService
    ) {
        this.me = this.tokenStorageService.getMe();
    }

    private refreshTokenStorage(): User {
        this.tokenStorageService.saveUser(this.me);
        return this.me;
    }

    createApplication(slug: string): Observable<Applicant> {
        return this.http.post<Applicant>(getApplicationUrl(slug), {}, httpOptions);
    }

    checkApplicantState(id: number): Observable<boolean> {
        return this.http.get<boolean>(getApplicationCheckUrl(id));
    }

    addStateAplicant(applicant: Applicant): User {
        this.me.applicants?.push({applicantId: applicant.id, slug: applicant.slug});
        return this.refreshTokenStorage();
    }
    
    addStateStudent(stateStudent: StateStudent): User {
        this.me.students?.push(stateStudent);
        return this.refreshTokenStorage();
    }
    
    newStudent(slug: string): Observable<StateStudent | undefined> {
        this.me.applicants!.splice(this.me.applicants!.findIndex(applicant => applicant.slug === slug), 1);
        return this.http.get<StateStudent>('/api/persons/students/' + slug);
    }
}
