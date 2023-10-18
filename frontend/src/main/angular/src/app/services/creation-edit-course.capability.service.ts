import { Injectable } from '@angular/core';
import { TokenStorageService } from './auth/token-storage.service';

const allowedRoles: string[] = ['MENTOR', 'ADMIN'];

@Injectable({ providedIn: 'root' })
export class CreationEditCourseCapabilityService {
    capability: boolean = false;
    editMode: boolean = false;

    constructor(
        private tokenStorageService: TokenStorageService
    ) {
        this.capability = tokenStorageService.haveIAnyRole(...allowedRoles);
    }

    hasCapability(): boolean {
        return this.capability;
    }

    setEditMode(mode: boolean): void {
        if (this.capability)
            this.editMode = mode;
    }

    getEditMode(): boolean {
        return this.editMode;
    }

}