import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActividadService } from '../service/actividad.service';
import { IActividad, Actividad } from '../actividad.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

import { ActividadUpdateComponent } from './actividad-update.component';

describe('Actividad Management Update Component', () => {
  let comp: ActividadUpdateComponent;
  let fixture: ComponentFixture<ActividadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let actividadService: ActividadService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActividadUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ActividadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActividadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    actividadService = TestBed.inject(ActividadService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleado query and add missing value', () => {
      const actividad: IActividad = { id: 456 };
      const empleado: IEmpleado = { id: 68260 };
      actividad.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 45684 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ actividad });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(empleadoCollection, ...additionalEmpleados);
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const actividad: IActividad = { id: 456 };
      const empleado: IEmpleado = { id: 21782 };
      actividad.empleado = empleado;

      activatedRoute.data = of({ actividad });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(actividad));
      expect(comp.empleadosSharedCollection).toContain(empleado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Actividad>>();
      const actividad = { id: 123 };
      jest.spyOn(actividadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actividad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: actividad }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(actividadService.update).toHaveBeenCalledWith(actividad);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Actividad>>();
      const actividad = new Actividad();
      jest.spyOn(actividadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actividad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: actividad }));
      saveSubject.complete();

      // THEN
      expect(actividadService.create).toHaveBeenCalledWith(actividad);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Actividad>>();
      const actividad = { id: 123 };
      jest.spyOn(actividadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actividad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(actividadService.update).toHaveBeenCalledWith(actividad);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmpleadoById', () => {
      it('Should return tracked Empleado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpleadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
