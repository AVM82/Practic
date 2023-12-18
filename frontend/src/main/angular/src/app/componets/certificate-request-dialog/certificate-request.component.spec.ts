import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateRequestComponent } from './certificate-request.component';

describe('CertificateRequestComponent', () => {
  let component: CertificateRequestComponent;
  let fixture: ComponentFixture<CertificateRequestComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CertificateRequestComponent]
    });
    fixture = TestBed.createComponent(CertificateRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
