import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyBtnComponent } from './apply-btn.component';

describe('ApplyBtnComponent', () => {
  let component: ApplyBtnComponent;
  let fixture: ComponentFixture<ApplyBtnComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ApplyBtnComponent]
    });
    fixture = TestBed.createComponent(ApplyBtnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
