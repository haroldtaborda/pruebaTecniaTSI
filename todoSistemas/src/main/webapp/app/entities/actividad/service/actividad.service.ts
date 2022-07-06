import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActividad, getActividadIdentifier } from '../actividad.model';

export type EntityResponseType = HttpResponse<IActividad>;
export type EntityArrayResponseType = HttpResponse<IActividad[]>;
export type StringType = HttpResponse<string>;

@Injectable({ providedIn: 'root' })
export class ActividadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/actividads');
  protected delayDaysUrl = this.applicationConfigService.getEndpointFor('api/activadsFindFelayDays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(actividad: IActividad): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(actividad);
    return this.http
      .post<IActividad>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getDelayDays(id: number): Observable<StringType> {
    return this.http.get<string>(`${this.delayDaysUrl}/${id}`, { observe: 'response' });
  }

  update(actividad: IActividad): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(actividad);
    return this.http
      .put<IActividad>(`${this.resourceUrl}/${getActividadIdentifier(actividad) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(actividad: IActividad): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(actividad);
    return this.http
      .patch<IActividad>(`${this.resourceUrl}/${getActividadIdentifier(actividad) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IActividad>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IActividad[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActividadToCollectionIfMissing(
    actividadCollection: IActividad[],
    ...actividadsToCheck: (IActividad | null | undefined)[]
  ): IActividad[] {
    const actividads: IActividad[] = actividadsToCheck.filter(isPresent);
    if (actividads.length > 0) {
      const actividadCollectionIdentifiers = actividadCollection.map(actividadItem => getActividadIdentifier(actividadItem)!);
      const actividadsToAdd = actividads.filter(actividadItem => {
        const actividadIdentifier = getActividadIdentifier(actividadItem);
        if (actividadIdentifier == null || actividadCollectionIdentifiers.includes(actividadIdentifier)) {
          return false;
        }
        actividadCollectionIdentifiers.push(actividadIdentifier);
        return true;
      });
      return [...actividadsToAdd, ...actividadCollection];
    }
    return actividadCollection;
  }

  protected convertDateFromClient(actividad: IActividad): IActividad {
    return Object.assign({}, actividad, {
      fechaEstimadaEjecucion: actividad.fechaEstimadaEjecucion?.isValid() ? actividad.fechaEstimadaEjecucion.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaEstimadaEjecucion = res.body.fechaEstimadaEjecucion ? dayjs(res.body.fechaEstimadaEjecucion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((actividad: IActividad) => {
        actividad.fechaEstimadaEjecucion = actividad.fechaEstimadaEjecucion ? dayjs(actividad.fechaEstimadaEjecucion) : undefined;
      });
    }
    return res;
  }
}
