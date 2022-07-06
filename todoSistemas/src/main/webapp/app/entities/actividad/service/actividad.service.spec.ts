import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoActividad } from 'app/entities/enumerations/estado-actividad.model';
import { IActividad, Actividad } from '../actividad.model';

import { ActividadService } from './actividad.service';

describe('Actividad Service', () => {
  let service: ActividadService;
  let httpMock: HttpTestingController;
  let elemDefault: IActividad;
  let expectedResult: IActividad | IActividad[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActividadService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      fechaEstimadaEjecucion: currentDate,
      descripcion: 'AAAAAAA',
      estado: EstadoActividad.PENDIENTE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaEstimadaEjecucion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Actividad', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaEstimadaEjecucion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEstimadaEjecucion: currentDate,
        },
        returnedFromService
      );

      service.create(new Actividad()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Actividad', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          fechaEstimadaEjecucion: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEstimadaEjecucion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Actividad', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          fechaEstimadaEjecucion: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        new Actividad()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaEstimadaEjecucion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Actividad', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          fechaEstimadaEjecucion: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEstimadaEjecucion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Actividad', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addActividadToCollectionIfMissing', () => {
      it('should add a Actividad to an empty array', () => {
        const actividad: IActividad = { id: 123 };
        expectedResult = service.addActividadToCollectionIfMissing([], actividad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(actividad);
      });

      it('should not add a Actividad to an array that contains it', () => {
        const actividad: IActividad = { id: 123 };
        const actividadCollection: IActividad[] = [
          {
            ...actividad,
          },
          { id: 456 },
        ];
        expectedResult = service.addActividadToCollectionIfMissing(actividadCollection, actividad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Actividad to an array that doesn't contain it", () => {
        const actividad: IActividad = { id: 123 };
        const actividadCollection: IActividad[] = [{ id: 456 }];
        expectedResult = service.addActividadToCollectionIfMissing(actividadCollection, actividad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(actividad);
      });

      it('should add only unique Actividad to an array', () => {
        const actividadArray: IActividad[] = [{ id: 123 }, { id: 456 }, { id: 74339 }];
        const actividadCollection: IActividad[] = [{ id: 123 }];
        expectedResult = service.addActividadToCollectionIfMissing(actividadCollection, ...actividadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const actividad: IActividad = { id: 123 };
        const actividad2: IActividad = { id: 456 };
        expectedResult = service.addActividadToCollectionIfMissing([], actividad, actividad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(actividad);
        expect(expectedResult).toContain(actividad2);
      });

      it('should accept null and undefined values', () => {
        const actividad: IActividad = { id: 123 };
        expectedResult = service.addActividadToCollectionIfMissing([], null, actividad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(actividad);
      });

      it('should return initial array if no Actividad is added', () => {
        const actividadCollection: IActividad[] = [{ id: 123 }];
        expectedResult = service.addActividadToCollectionIfMissing(actividadCollection, undefined, null);
        expect(expectedResult).toEqual(actividadCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
