import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ActividadService } from '../service/actividad.service';

import { ActividadComponent } from './actividad.component';

describe('Actividad Management Component', () => {
  let comp: ActividadComponent;
  let fixture: ComponentFixture<ActividadComponent>;
  let service: ActividadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ActividadComponent],
    })
      .overrideTemplate(ActividadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActividadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ActividadService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.actividads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
