import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InactivePersonComponent } from './inactive-person.component';

describe('InactivePersonComponent', () => {
  let component: InactivePersonComponent;
  let fixture: ComponentFixture<InactivePersonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InactivePersonComponent]
    });
    fixture = TestBed.createComponent(InactivePersonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
